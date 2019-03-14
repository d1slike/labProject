package ru.stankin.updater;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import ru.stankin.utils.Util;
import ru.stankin.utils.files.FileUtils;

import java.io.*;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by Dislike on 27.02.2016.
 */
public class ApplicationUpdater extends Service<UpdateStatus> {

    private static final String APPLICATION_TOKEN = "smHMgyG3CFAAAAAAAAAABzbibSgnKcSxEFORi_l0zNkSaK7hWlOrTQICvuHdGxcC";
    private static final String REMOTE_VER_FILE_NAME = "remote_ver.ini";
    private static final String VERSION_FILE_NAME = "version.ini";
    private static final String RESOURCES_FILE_NAME = "resources";
    private static final String LOCAL_VER_FILE_NAME = Util.externalResource(VERSION_FILE_NAME);
    private static final String LOCAL_RESOURCES_DIRECTORY_NAME = Util.externalResource(RESOURCES_FILE_NAME);
    private static final String REMOTE_RESOURCES_DIRECTORY_NAME = "remote_resources";

    public static final String UPDATE_STATE_CHECK_NEED_UPDATE = "Проверка наличия обновлений...";
    public static final String UPDATE_STATE_UPDATING = "Обновление...";
    public static final String UPDATE_STATE_PROGRAM_RUNNING = "Запуск программы...";

    private static final Version DEFAULT_VERSION = new Version(1,0,0);

    private DbxClientV2 client;
    private UpdateWindowController controller;
    private Version currentVersion;
    private Version newVersion;


    public ApplicationUpdater(UpdateWindowController controller) {
        DbxRequestConfig config = new DbxRequestConfig("LabProjectClient", Locale.getDefault().toString());
        client = new DbxClientV2(config, APPLICATION_TOKEN);
        this.controller = controller;
    }

    public Version getCurrentVersion() {
        return currentVersion == null ? DEFAULT_VERSION : currentVersion;
    }


    @Override
    protected Task<UpdateStatus> createTask() {
        return new UpdateTask();
    }

    private class UpdateTask extends Task<UpdateStatus> {

        private UpdateStatus currentUpdateStatus;
        private long resourcesDirSize;
        private long alreadyDownloaded;

        public UpdateTask() {
            currentUpdateStatus = UpdateStatus.NOT_NEED_UPDATE;
            alreadyDownloaded = 0;
            controller.setCurrentStateText(UPDATE_STATE_CHECK_NEED_UPDATE);
        }

        @Override
        protected UpdateStatus call() throws Exception {
            if (!testNetConnection())
                return UpdateStatus.HAVE_NO_NET_CONNECTION;
            UpdateStatus status = update();
            controller.setCurrentStateText(UPDATE_STATE_PROGRAM_RUNNING);
            Thread.sleep(1000);
            return status;
        }

        private boolean testNetConnection() {
            boolean ok = false;
            try {
                ok = client.users().getCurrentAccount() != null;
            } catch (DbxException ignored) {
                ignored.printStackTrace();
            }
            return ok;
        }

        private void getResourcesDirSize(String path) {
            try {
                List<Metadata> metadataList = client.files().listFolder(path).getEntries();
                for (Metadata metadata : metadataList) {
                    if (metadata instanceof FolderMetadata) {
                        String dirName = new StringBuilder(path).append("/").append(metadata.getName()).toString();
                        getResourcesDirSize(dirName);
                    } else if (metadata instanceof FileMetadata)
                        resourcesDirSize += ((FileMetadata) metadata).getSize();
                }
            } catch (DbxException e) {
                e.printStackTrace();
                currentUpdateStatus = UpdateStatus.FAIL;
            }
        }


        private UpdateStatus update() {
            if (checkForNeedUpdate()) {
                if (new File(REMOTE_RESOURCES_DIRECTORY_NAME).mkdir()) {
                    getResourcesDirSize("/" + REMOTE_RESOURCES_DIRECTORY_NAME);
                    updateProgress(0, resourcesDirSize);
                    currentUpdateStatus = UpdateStatus.PREPARED;
                    controller.setCurrentStateText(UPDATE_STATE_UPDATING);
                    downloadResource("/" + REMOTE_RESOURCES_DIRECTORY_NAME);
                    if (currentUpdateStatus != UpdateStatus.FAIL) {
                        FileUtils.deleteFiles(LOCAL_RESOURCES_DIRECTORY_NAME);
                        FileUtils.rename(REMOTE_RESOURCES_DIRECTORY_NAME, LOCAL_RESOURCES_DIRECTORY_NAME);
                        FileUtils.deleteFiles(LOCAL_VER_FILE_NAME);
                        FileUtils.rename(REMOTE_VER_FILE_NAME, LOCAL_VER_FILE_NAME);
                        currentVersion = newVersion;
                        currentUpdateStatus = UpdateStatus.SUCCESS;
                    }
                }

            }
            return currentUpdateStatus;
        }

        private void downloadResource(String path) {
            try {
                List<Metadata> metadataList = client.files().listFolder(path).getEntries();
                for (Metadata metadata : metadataList) {
                    if (metadata instanceof FolderMetadata) {
                        String dirName = new StringBuilder(path).append("/").append(metadata.getName()).toString();
                        new File(dirName.substring(1)).mkdirs();
                        downloadResource(dirName);
                    } else if (metadata instanceof FileMetadata) {
                        String fileLocation = metadata.getPathDisplay();
                        try (OutputStream stream = new FileOutputStream(fileLocation.substring(1))) {
                            client.files().download(fileLocation).download(stream);
                            alreadyDownloaded += ((FileMetadata) metadata).getSize();
                            updateProgress(alreadyDownloaded, resourcesDirSize);
                        } catch (IOException e) {
                            e.printStackTrace();
                            currentUpdateStatus = UpdateStatus.FAIL;
                        }
                    }
                }
            } catch (DbxException e) {
                e.printStackTrace();
                currentUpdateStatus = UpdateStatus.FAIL;
            }
        }

        private boolean checkForNeedUpdate() {
            Version localVersion = getVersionFromFile(LOCAL_VER_FILE_NAME);
            if (localVersion == null) {
                currentUpdateStatus = UpdateStatus.LOCAL_FILE_VERSION_IS_BAD;
                return false;
            }
            currentVersion = localVersion;
            Version remoteVersion = downloadAndGetRemoteVersion();
            boolean needUpdate = remoteVersion != null && remoteVersion.isOlderThen(localVersion);
            if (!needUpdate)
                FileUtils.deleteFiles(REMOTE_VER_FILE_NAME);
            newVersion = remoteVersion;
            return needUpdate;

        }

        private Version downloadAndGetRemoteVersion() {
            Version remote = null;
            try (OutputStream outputStream = new FileOutputStream(REMOTE_VER_FILE_NAME)) {
                DbxDownloader<FileMetadata> dbxDownloader = client.files().download("/" + VERSION_FILE_NAME);
                dbxDownloader.download(outputStream);
                outputStream.close();
                File local = new File(REMOTE_VER_FILE_NAME);
                if (local.exists())
                    remote = getVersionFromFile(REMOTE_VER_FILE_NAME);
                //local.delete();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return remote;
        }

        private Version getVersionFromFile(String fileName) {
            Version version = null;
            try (BufferedReader inputStream = new BufferedReader(new FileReader(fileName))) {
                version = parseVersion(inputStream.readLine());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return version;
        }

        private Version parseVersion(String versionInString) throws Exception {
            StringTokenizer tokenizer = new StringTokenizer(versionInString, ".");
            int main = Integer.parseInt(tokenizer.nextToken());
            int sub = Integer.parseInt(tokenizer.nextToken());
            int rev = Integer.parseInt(tokenizer.nextToken());
            return new Version(main, sub, rev);
        }
    }


    public void terminate() {
        controller = null;
        client = null;
    }

}
