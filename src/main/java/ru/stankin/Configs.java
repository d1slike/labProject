package ru.stankin;


import jfork.nproperty.ConfigParser;
import ru.stankin.test.model.Test;
import ru.stankin.utils.Util;
import ru.stankin.utils.files.CipherFileStreamFactory;

import java.io.InputStream;

/**
 * Created by Dislike on 04.03.2016.
 */
public class Configs {

    private static final String PROPERTY_FILE = "properties.ini";

    public static void load() {
        try {
            InputStream stream = CipherFileStreamFactory.getInstance().getSafeFileInputStream(PROPERTY_FILE);
            ConfigParser.parse(Test.class,
                    stream,
                    PROPERTY_FILE);
        } catch (Exception ex) {
            Util.showProgramsFilesSpoiled();
        }
    }

}
