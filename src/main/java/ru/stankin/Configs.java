package ru.stankin;


import org.apache.commons.lang3.reflect.FieldUtils;
import ru.stankin.utils.Util;
import ru.stankin.utils.files.CipherFileStreamFactory;

import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by Dislike on 04.03.2016.
 */
public class Configs {

    private static final String PROPERTY_FILE = Util.externalResource("resources/properties.ini");

    //@Cfg("MaxQuestions")
    private static int MAX_QUESTIONS = 15;
    @Cfg("MinutesToCompleteTest")
    private static int MAX_MINUTES_TO_COMPLETE = 20;
    @Cfg("AttemptsToCompleteTestWithoutBadMark")
    private static int MAX_ATTEMPTS = 2;
    //@Cfg("MinCorrectAnswersCountToCompleteTest")
    private static int MIN_CORRECT_ANSWERS_TO_COMPLETE = 8;
    @Cfg("PointSForCorrectAnswer")
    private static double POINTS_FOR_CORRECT_ANSWER = 2.6;

    @Cfg("TimeStepsInWork")
    private static int TIME_STEPS_IN_WORK = 11;
    @Cfg("AltVarStepsInWork")
    private static int ALT_VAR_STEPS_IN_WORK = 4;

    static void load() {
        try(InputStream stream = CipherFileStreamFactory.getInstance().getSafeFileInputStream(PROPERTY_FILE)) {
            Properties properties = new Properties();
            properties.load(stream);

            Map<String, Field> fieldMap = FieldUtils.getFieldsListWithAnnotation(Configs.class, Cfg.class).stream()
                    .filter(field -> {
                        int modifiers = field.getModifiers();
                        return Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers);
                    }).peek(field -> field.setAccessible(true)).collect(Collectors.toMap(field -> field.getAnnotation(Cfg.class).value(), field -> field));
            properties.forEach((k, v) -> {
                fieldMap.computeIfPresent(String.valueOf(k), (name, field) -> {
                    Object value;
                    String stringValue = String.valueOf(v);
                    Class<?> type = field.getType();
                    try {
                        if (type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class)) {
                            value = Integer.parseInt(stringValue);
                        } else if (type.isAssignableFrom(boolean.class) || type.isAssignableFrom(Boolean.class)) {
                            value = Boolean.parseBoolean(stringValue);
                        } else if (type.isAssignableFrom(float.class) || type.isAssignableFrom(Float.class)) {
                            value = Float.parseFloat(stringValue);
                        } else if (type.isAssignableFrom(double.class) || type.isAssignableFrom(Double.class)) {
                            value = Double.parseDouble(stringValue);
                        } else {
                            value = stringValue;
                        }
                        FieldUtils.writeStaticField(field, value);

                    } catch (IllegalAccessException ignored) {

                    }
                    return field;
                });
            });

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

    public static class Lab {
        public static int timeSteps() {
            return TIME_STEPS_IN_WORK;
        }

        public static int altVarSteps() {
            return ALT_VAR_STEPS_IN_WORK;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    private @interface Cfg {
        String value();
    }

}
