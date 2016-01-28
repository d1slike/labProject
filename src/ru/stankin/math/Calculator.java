package ru.stankin.math;

import ru.stankin.holders.VariableHolder;
import ru.stankin.model.ResultRecord;
import ru.stankin.enums.VariableType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dislike on 23.01.2016.
 */
public class Calculator {
    private static final double G = 9.8066;
    private static final double TWO = 2D;
    private static final double TWENTY_FOURTH = 24D;
    private static final double EIGHT = 8;
    private static final String OMEGA_IN_QRT = "omegaInQrt";
    private static final String EPSILON = "epsilon";
    private static final String MASS = "m";
    private static final String IXZ = "Ixz";
    private static final String IYZ = "Iyz";
    private static final String XC = "xc";
    private static final String YC = "yc";
    private static final String H = "h";

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
        double mass = Math.PI;
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
        final VariableType researchVarType = variableHolder.getResearchVariable();
        Map<String, Double> varCache = new HashMap<>();
        varCache.put(EPSILON, getEpsilon());
        varCache.put(OMEGA_IN_QRT, Math.pow(getOmega(), TWO));
        varCache.put(MASS, getMass());
        varCache.put(IYZ, getIyz());
        varCache.put(IXZ, getIxz());
        varCache.put(XC, getXc());
        varCache.put(YC, getYc());
        varCache.put(H, getVar(VariableType.H));
        double staticReact = getReaction(true, researchVarType, varCache);
        double dynamicReact = getReaction(false, researchVarType, varCache);
        return new ResultRecord(
                getVar(VariableType.T),
                variableHolder.getAltVariable().getValue(),
                staticReact,
                dynamicReact,
                staticReact + dynamicReact,
                calcRPM()
        );
    }

    private static double getReaction(boolean isStatic, VariableType type, Map<String, Double> varCache) {
        double epsilon = isStatic ? 0 : varCache.get(EPSILON);
        double omegaInQrt = isStatic ? 0 : varCache.get(OMEGA_IN_QRT);
        double m = varCache.get(MASS);
        double Ixz = varCache.get(IXZ);
        double Iyz = varCache.get(IYZ);
        double xc = varCache.get(XC);
        double yc = varCache.get(YC);
        double h = varCache.get(H);

        switch (type) {
            case Ya:
            case Yb:
                double Yb = (epsilon * Ixz - omegaInQrt * Iyz) / h;
                if (type == VariableType.Ya)
                    return (m * xc * epsilon) - (m * yc * omegaInQrt) - Yb;
                return Yb;
            case Xa:
            case Xb:
                double Xb = ((m * G * getVar(VariableType.Zc)) - (epsilon * Iyz) - (omegaInQrt * Ixz)) / h;
                if (type == VariableType.Xa)
                    return (m * G) - (m * yc * epsilon) - (m * xc * omegaInQrt) - Xb;
                return Xb;
        }

        return Double.MAX_VALUE;
    }

    public static double calcRPM() {
        return 0;//todo fix
    }


}
