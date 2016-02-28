package ru.stankin.utils;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;

import java.io.*;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by Dislike on 27.02.2016.
 */
public class ApplicationUpdater {

    private static final String APPLICATION_TOKEN = "smHMgyG3CFAAAAAAAAAABzbibSgnKcSxEFORi_l0zNkSaK7hWlOrTQICvuHdGxcC";
    private static final String REMOTE_VER_FILE_NAME = "remote_ver.ini";
    private static final String LOCAL_VER_FILE_NAME = "version.ini";
    private static final String RESOURCES_DIRECTORY_NAME = "res";

    private DbxClientV2 client;
    private UpdateStatus currentUpdateStatus;

    public ApplicationUpdater() {
        currentUpdateStatus = UpdateStatus.NOT_NEED_UPDATE;
    }

    public UpdateStatus update() {
        DbxRequestConfig config = new DbxRequestConfig("LabProjectClient", Locale.getDefault().toString());
        client = new DbxClientV2(config, APPLICATION_TOKEN);
        if (checkForNeedUpdate()) {
            if (deleteFiles(RESOURCES_DIRECTORY_NAME)) {
                if (new File(RESOURCES_DIRECTORY_NAME).mkdir()) {
                    currentUpdateStatus = UpdateStatus.PREPARED;
                    downloadResource("/" + RESOURCES_DIRECTORY_NAME);
                    if (currentUpdateStatus != UpdateStatus.FAIL) {
                        currentUpdateStatus = UpdateStatus.SUCCESS;
                        deleteFiles(LOCAL_VER_FILE_NAME);
                        new File(REMOTE_VER_FILE_NAME).renameTo( new File(LOCAL_VER_FILE_NAME));
                    }
                }
            }
        }

        return currentUpdateStatus;
    }

    private boolean deleteFiles(String fileOrDirectoryName) {
        File resourcesDirectory = new File(fileOrDirectoryName);
        return delete(resourcesDirectory);
    }

    private static boolean delete(File file) {
        if (file.isDirectory())
            for (File inFile : file.listFiles())
                delete(inFile);
        return file.delete();
    }


    private void downloadResource(String path) {
        try {
            List<Metadata> metadataList = client.files.listFolder(path).getEntries();
            for (Metadata metadata : metadataList) {
                if (metadata instanceof FolderMetadata) {
                    String dirName = new StringBuilder(path).append("/").append(metadata.getName()).toString();
                    new File(dirName.substring(1)).mkdirs();
                    downloadResource(dirName);
                } else if (metadata instanceof FileMetadata) {
                    String fileLocation = metadata.getPathDisplay();
                    try (OutputStream stream = new FileOutputStream(fileLocation.substring(1))) {
                        client.files.download(fileLocation).download(stream);
                    } catch (IOException e) {
                        currentUpdateStatus = UpdateStatus.FAIL;
                    }
                }
            }
        } catch (DbxException e) {
            currentUpdateStatus = UpdateStatus.FAIL;
        }
    }

    private boolean checkForNeedUpdate() {
        Version localVersion = getVersionFromFile(LOCAL_VER_FILE_NAME);
        if (localVersion == null) {
            Util.showProgramsFilesSpoiled();
            return false;
        }
        Version remoteVersion = downloadAndGetRemoteVersion();
        boolean needUpdate = remoteVersion != null && remoteVersion.isOlderThen(localVersion);
        if (!needUpdate)
            deleteFiles(REMOTE_VER_FILE_NAME);
        return needUpdate;

    }

    private Version downloadAndGetRemoteVersion() {
        Version remote = null;
        try (OutputStream outputStream = new FileOutputStream(REMOTE_VER_FILE_NAME)) {
            DbxDownloader<FileMetadata> dbxDownloader = client.files.download("/" + LOCAL_VER_FILE_NAME);
            dbxDownloader.download(outputStream);
            outputStream.close();
            File local = new File(REMOTE_VER_FILE_NAME);
            if (local.exists())
                remote = getVersionFromFile(REMOTE_VER_FILE_NAME);
            //local.delete();
        } catch (Exception ex) {
            //ex.printStackTrace();
        }

        return remote;
    }

    private Version getVersionFromFile(String fileName) {
        Version version = null;
        try (BufferedReader inputStream = new BufferedReader(new FileReader(fileName))) {
            version = parseVersion(inputStream.readLine());
        } catch (Exception ex) {
            //ex.printStackTrace();
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

    private static class Version {
        private final int mainVersion;
        private final int subVersion;
        private final int revision;

        private Version(int mainVersion, int subVersion, int revision) {
            this.mainVersion = mainVersion;
            this.subVersion = subVersion;
            this.revision = revision;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (obj.getClass() != getClass())
                return false;
            Version version = (Version) obj;
            return version.mainVersion == mainVersion && version.subVersion == subVersion && revision == version.revision;
        }

        public boolean isOlderThen(Version ver) {
            if (mainVersion > ver.mainVersion)
                return true;
            else if (mainVersion == ver.mainVersion && subVersion > ver.subVersion)
                return true;
            else if (mainVersion == ver.mainVersion && subVersion == ver.subVersion && revision > ver.revision)
                return true;
            return false;
        }
    }

    public enum UpdateStatus {
        PREPARED, SUCCESS, FAIL, NOT_NEED_UPDATE;
    }
}