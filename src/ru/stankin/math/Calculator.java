package ru.stankin.math;

import ru.stankin.holders.VariableHolder;
import ru.stankin.model.ResultRecord;
import ru.stankin.model.VariableType;

/**
 * Created by Dislike on 23.01.2016.
 */
public class Calculator {
    private static final double G = 9.8066;
    private static final double TWO = 2D;
    private static final double TWENTY_FOURTH = 24D;
    private static final double EIGHT = 8;
    private static VariableHolder variableHolder;

    private static double currentTime;

    public static void initVarHolder(VariableHolder holder) {
        variableHolder = holder;
    }

    private static double getXc() {
        return Math.sin(getPhi()) * getVar(VariableType.E);
    }

    private static double getYc() {
        return Math.cos(getPhi()) * getVar(VariableType.E);
    }

    private static double getMass() {
        double mass = TWO * Math.PI;
        mass *= Math.pow(getVar(VariableType.R), TWO);
        mass *= getVar(VariableType.RO);
        mass *= getVar(VariableType.L);

        return mass;
    }

    private static double getOmega() {
        return getEpsilon() * getVar(VariableType.T);
    }

    private static double getPhi() {
        return getEpsilon() * Math.pow(getVar(VariableType.T), TWO) / TWO;
    }

    private static double getEpsilon() {
        return getVar(VariableType.M) / getIz();
    }

    private static double getIz() {
        return 0; //TODO ?
    }

    private static double getConstA() {
        double bracketResult = Math.pow(getVar(VariableType.L), TWO) / TWENTY_FOURTH - Math.pow(getVar(VariableType.R), TWO) / EIGHT;
        double m = getMass();
        double firstSummand = m * bracketResult * Math.sin(TWO * getVar(VariableType.GAMMA));
        double secondSummand = m * getVar(VariableType.Zc) * getYc();
        return firstSummand + secondSummand;
    }

    private static double getIxz() {
        return -getConstA() * Math.sin(getPhi());
    }

    private static double getIyz() {
        return getConstA() * Math.cos(getPhi());
    }

    private static double getVar(VariableType type) {
        return type == VariableType.T ? currentTime : variableHolder.getVarValue(type);
    }

    public static ResultRecord calculateReactions(double time) {
        currentTime = time;
        return null;
    }
}
