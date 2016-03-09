package ru.stankin;


import jfork.nproperty.Cfg;
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

    @Cfg("MaxQuestions")
    private static int MAX_QUESTIONS = 10;
    @Cfg("MinutesToCompleteTest")
    private static int MAX_MINUTES_TO_COMPLETE = 19;
    @Cfg("AttemptsToCompleteTestWithoutBadMark")
    private static int MAX_ATTEMPTS = 2;
    @Cfg("MinCorrectAnswersCountToCompleteTest")
    private static int MIN_CORRECT_ANSWERS_TO_COMPLETE = 6;
    @Cfg("MinMark")
    private static int MIN_MARK = 20;
    @Cfg("MaxMark")
    private static int MAX_MARK = 40;

    static {
        load();
    }

    private static void load() {
        try {
            InputStream stream = CipherFileStreamFactory.getInstance().getSafeFileInputStream(PROPERTY_FILE);
            ConfigParser.parse(Configs.class,
                    stream,
                    PROPERTY_FILE);
        } catch (Exception ex) {
            Util.showProgramsFilesSpoiled();
        }
    }

    public static class Test {
        public static int maxQuestions() {
            return MAX_QUESTIONS;
        }

        public static int minutesToComplete() {
            return MAX_MINUTES_TO_COMPLETE;
        }

        public static int attempts() {
            return MAX_ATTEMPTS;
        }

        public static int correctAnswersToComplete() {
            return MIN_CORRECT_ANSWERS_TO_COMPLETE;
        }

        public static int minMark() {
            return MIN_MARK;
        }

        public static int maxMark() {
            return MAX_MARK;
        }
    }

}
