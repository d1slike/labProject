package ru.stankin.utils;

import java.util.List;
import java.util.Random;

/**
 * Created by DisDev on 05.02.2016.
 */
public class Rnd {
    private static final Random random = new Random();

    public static int get(int min, int max) {
        if (min >= max)
            return Integer.MIN_VALUE;
        return random.nextInt((max - min) + 1) + min;
    }

    public static int get(int max) {
        return random.nextInt(max);
    }

    public static <E> E get(List<E> list) {
        return list.get(get(list.size()));
    }

    public static <E> E get(E[] array) {
        return array[get(array.length)];
    }
}
