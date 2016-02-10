package ru.stankin.work.math;

import ru.stankin.work.managers.VariableManager;
import ru.stankin.work.model.ResultRecord;
import ru.stankin.enums.VariableType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dislike on 23.01.2016.
 */
public class Calculator {
    private static final double G = 9.81;
    private static final double TWO = 2.0;
    //private static final double TWENTY_FOURTH = 24D;
    //private static final double EIGHT = 8;
    private static final String OMEGA_IN_SQR = "omegaInSqr";
    private static final String EPSILON = "epsilon";
    private static final String MASS = "m";
    private static final String IXZ = "Ixz";
    private static final String IYZ = "Iyz";
    private static final String XC = "xc";
    private static final String YC = "yc";
    private static final String H = "h";

    private final VariableManager variableManager;
    private double currentTime;

    private double getXc() {
        return getVar(VariableType.E) * Math.sin(getPhiInRadians());
    }

    private double getYc() {
        return getVar(VariableType.E) * Math.cos(getPhiInRadians());
    }

    private double getMass() {
        double mass = Math.PI;
        mass *= Math.pow(getVar(VariableType.R), TWO);
        mass *= getVar(VariableType.RO);
        mass *= getVar(VariableType.L);

        return mass;
    }

    private double getOmega() {
        return getEpsilon() * getVar(VariableType.T);
    }

    private double getPhiInRadians() {
        return Math.toRadians(getEpsilon() * Math.pow(getVar(VariableType.T), TWO) / TWO);
    }

    private double getEpsilon() {
        return getVar(VariableType.M) / getIz();
    }

    private double getIz() {
        //return getMass() * Math.pow(getVar(VariableType.R), TWO) / TWO; //TODO уточнить
        double m = getMass();
        double lInSqr = Math.pow(getVar(VariableType.L), TWO);
        double rInSqr = Math.pow(getVar(VariableType.R), TWO);
        double gammaInRadians = Math.toRadians(getVar(VariableType.GAMMA));
        double sinInQrt = Math.pow(Math.sin(gammaInRadians), TWO);
        double cosInQrt = Math.pow(Math.cos(gammaInRadians), TWO);
        double firstSummand = (m * lInSqr / 12D + m * rInSqr / 4D) * sinInQrt;
        double secondSummand = m * rInSqr / TWO * cosInQrt;
        return firstSummand + secondSummand;
    }

    private double getConstA() {
        double bracketResult = Math.pow(getVar(VariableType.L), TWO) / 24D - Math.pow(getVar(VariableType.R), TWO) / 8D;
        double m = getMass();
        double firstSummand = m * bracketResult * Math.sin(Math.toRadians(TWO * getVar(VariableType.GAMMA)));
        double secondSummand = m * getVar(VariableType.Zc) * getYc();
        return firstSummand + secondSummand;
    }

    private double getIxz() {
        return -getConstA() * Math.sin(getPhiInRadians());
    }

    private double getIyz() {
        return getConstA() * Math.cos(getPhiInRadians());
    }

    private double getVar(VariableType type) {
        return type == VariableType.T ? currentTime : variableManager.getVarValue(type);
    }

    public ResultRecord calculateReactions(double time) {
        currentTime = time;
        final VariableType researchVarType = variableManager.getResearchVariable();
        Map<String, Double> varCache = new HashMap<>();
        varCache.put(EPSILON, getEpsilon());
        varCache.put(OMEGA_IN_SQR, Math.pow(getOmega(), TWO));
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
                variableManager.getAltVariable().getValue(),
                staticReact,
                dynamicReact,
                staticReact + dynamicReact,
                calcRPM()
        );
    }

    private double getReaction(boolean isStatic, VariableType type, Map<String, Double> varCache) {
        final double epsilon = isStatic ? 0 : varCache.get(EPSILON);
        final double omegaInSqr = isStatic ? 0 : varCache.get(OMEGA_IN_SQR);
        final double m = varCache.get(MASS);
        final double Ixz = varCache.get(IXZ);
        final double Iyz = varCache.get(IYZ);
        final double xc = varCache.get(XC);
        final double yc = varCache.get(YC);
        final double h = varCache.get(H);

        switch (type) {
            case Ya:
            case Yb:
                double Yb = (epsilon * Ixz - omegaInSqr * Iyz) / h;
                if (type == VariableType.Ya)
                    return (m * xc * epsilon) - (m * yc * omegaInSqr) - Yb;
                return Yb;
            case Xa:
            case Xb:
                double Xb = ((m * G * getVar(VariableType.Zc)) - (epsilon * Iyz) - (omegaInSqr * Ixz)) / h;
                if (type == VariableType.Xa)
                    return (m * G) - (m * yc * epsilon) - (m * xc * omegaInSqr) - Xb;
                return Xb;
        }

        return Double.MAX_VALUE;
    }

    public long calcRPM() {
        return Math.round(getOmega() * getVar(VariableType.T) / (TWO * Math.PI * 60D)); //todo check
    }

    public Calculator(VariableManager variableManager) {
        this.variableManager = variableManager;
    }
}
