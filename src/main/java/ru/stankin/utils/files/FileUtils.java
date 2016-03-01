package ru.stankin.utils.files;

import java.io.File;

/**
 * Created by Dislike on 02.03.2016.
 */
public class FileUtils {
    public static boolean deleteFiles(String fileOrDirectoryName) {
        File resourcesDirectory = new File(fileOrDirectoryName);
        return delete(resourcesDirectory);
    }

    public static boolean rename(String oldFileName, String newFileName) {
        return new File(oldFileName).renameTo(new File(newFileName));
    }

    private static boolean delete(File file) {
        if (file.isDirectory())
            for (File inFile : file.listFiles())
                delete(inFile);
        return file.delete();
    }
}
