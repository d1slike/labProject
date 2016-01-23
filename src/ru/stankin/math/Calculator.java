package ru.stankin.math;

import ru.stankin.holders.VariableHolder;

/**
 * Created by Dislike on 23.01.2016.
 */
public class Calculator {
    private static VariableHolder variableHolder;

    public static void initVarHolder(VariableHolder holder) {
        variableHolder = holder;
    }
}
