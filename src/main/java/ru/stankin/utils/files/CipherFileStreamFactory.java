package ru.stankin.utils.files;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Dislike on 02.03.2016.
 */
public class CipherFileStreamFactory {
    private static CipherFileStreamFactory ourInstance = new CipherFileStreamFactory();
    private SecretKeySpec key;

    private CipherFileStreamFactory() {
        key = null;
        try {
            key = new SecretKeySpec("key".getBytes("UTF-8"), "Blowfish");
        } catch (UnsupportedEncodingException ignored) {
        }

    }

    public InputStream getSafeFileInputStream(String pathToFile) {
        return getSafeFileInputStream(new File(pathToFile));
    }

    public InputStream getSafeFileInputStream(File file) {
        InputStream inputStream = null;
        try {
            Cipher cipher = Cipher.getInstance("Blowfish");
            if (key != null) {
                cipher.init(Cipher.DECRYPT_MODE, key);
                inputStream = new CipherInputStream(new FileInputStream(file), cipher);
            }

        } catch (NoSuchPaddingException | FileNotFoundException | InvalidKeyException | NoSuchAlgorithmException e) {
        }
        return inputStream;
    }

    public static CipherFileStreamFactory getInstance() {
        return ourInstance;
    }

}
