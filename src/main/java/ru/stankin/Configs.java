package ru.stankin;


import jfork.nproperty.Cfg;
import jfork.nproperty.ConfigParser;
import ru.stankin.utils.Util;
import ru.stankin.utils.files.CipherFileStreamFactory;

import java.io.InputStream;

/**
 * Created by Dislike on 04.03.2016.
 */
public class Configs {

    private static final String PROPERTY_FILE = "resources/properties.ini";

    @Cfg("MaxQuestions")
    private static int MAX_QUESTIONS = 2;
    @Cfg("MinutesToCompleteTest")
    private static int MAX_MINUTES_TO_COMPLETE = 20;
    @Cfg("AttemptsToCompleteTestWithoutBadMark")
    private static int MAX_ATTEMPTS = 2;
    @Cfg("MinCorrectAnswersCountToCompleteTest")
    private static int MIN_CORRECT_ANSWERS_TO_COMPLETE = 1;
    /*@Cfg("MinMark")
    private static int MIN_MARK = 3;
    @Cfg("MaxMark")
    private static int MAX_MARK = 5;*/
    @Cfg("PointSForCorrectAnswer")
    private static double POINTS_FOR_CORRECT_ANSWER = 2.6;

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
        private static int minPoints = (int) Math.round(MIN_CORRECT_ANSWERS_TO_COMPLETE * POINTS_FOR_CORRECT_ANSWER);
        private static int maxPoints = (int) Math.round(MAX_QUESTIONS * POINTS_FOR_CORRECT_ANSWER);

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

        public static int minPoints() {
            return minPoints;
        }

        public static int maxPoints() {
            return maxPoints;
        }

        public static double pointsForCorrectAnswer() {
            return POINTS_FOR_CORRECT_ANSWER;
        }


    }

}
