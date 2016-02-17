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
    private static final double DEGREES_180 = 180D;
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
    private double omegaForCalculatingNextTime;

    public void initOmegaForNextTimeCalculate() {
        omegaForCalculatingNextTime = getOmega(getVar(VariableType.TAU));
    }

    public double getNextTime() {
        return omegaForCalculatingNextTime / getEpsilon();
    }

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

    private double getOmega(double time) {
        return getEpsilon() * time;
    }

    private double getPhiInRadians() {

        return Math.toRadians(getPhiInDegrees(getVar(VariableType.T)));
    }

    public double getPhiInDegrees(double time) {
        return DEGREES_180 / Math.PI * ((getEpsilon() / TWO * Math.pow(time, TWO)) % (TWO * Math.PI));
    }

    private double getEpsilon() {
        return getVar(VariableType.M) / getIz();
    }

    private double getIz() {
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
    public ResultRecord calculateReactions(double time, int pointNum) {
        currentTime = time;
        final VariableType researchVarType = variableManager.getResearchVariable();
        Map<String, Double> varCache = new HashMap<>();
        varCache.put(EPSILON, getEpsilon());
        varCache.put(OMEGA_IN_SQR, Math.pow(getOmega(currentTime), TWO));
        varCache.put(MASS, getMass());
        varCache.put(IYZ, getIyz());
        varCache.put(IXZ, getIxz());
        varCache.put(XC, getXc());
        varCache.put(YC, getYc());
        varCache.put(H, getVar(VariableType.H));
        double staticReact = getReaction(true, researchVarType, varCache);
        double dynamicReact = getReaction(false, researchVarType, varCache);
        return new ResultRecord(
                pointNum,
                currentTime,
                variableManager.getAltVariable().getValue(),
                staticReact,
                dynamicReact,
                staticReact + dynamicReact,
                getPhiInDegrees(currentTime)
        );
    }

    private double getReaction(boolean isStatic, VariableType researchValType, Map<String, Double> varCache) {
        final double epsilon = varCache.get(EPSILON);
        final double omegaInSqr = varCache.get(OMEGA_IN_SQR);
        final double m = varCache.get(MASS);
        final double Ixz = varCache.get(IXZ);
        final double Iyz = varCache.get(IYZ);
        final double xc = varCache.get(XC);
        final double yc = varCache.get(YC);
        final double h = varCache.get(H);

        if (isStatic) {
            switch (researchValType) {
                case Ya:
                case Yb:
                        return 0D;
                case Xa:
                case Xb:
                    double Xb = ((m * G * getVar(VariableType.Zc))) / h;
                    if (researchValType == VariableType.Xa)
                        return (m * G) - Xb;
                    return Xb;
            }
        } else {
            switch (researchValType) {
                case Ya:
                case Yb:
                    double Yb = (epsilon * Ixz - omegaInSqr * Iyz) / h;
                    if (researchValType == VariableType.Ya)
                        return (m * xc * epsilon) - (m * yc * omegaInSqr) - Yb;
                    return Yb;
                case Xa:
                case Xb:
                    double Xb = (-(epsilon * Iyz) - (omegaInSqr * Ixz)) / h;
                    if (researchValType == VariableType.Xa)
                        return -(m * yc * epsilon) - (m * xc * omegaInSqr) - Xb;
                    return Xb;
            }
        }

        return Double.MAX_VALUE;
    }

    public long calcRPMForTau() {
        return (long) Math.ceil((getOmega(getVar(VariableType.TAU)) * 30D) / Math.PI);
    }


    public Calculator(VariableManager variableManager) {
        this.variableManager = variableManager;
    }
}
