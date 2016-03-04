package ru.stankin.enums;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Dislike on 22.01.2016.
 */
public enum  VariableType {

    RO(VarName.RO, -33, 8, "Кг/м\u00B2"),
    R(VarName.R, -54, 44, "м"),
    L(VarName.L, -300, 80, "м"),
    E(VarName.E, 0, 12, "м"),
    Zc(VarName.Zc, 30, 285, "м"),
    GAMMA(VarName.GAMMA, 0, 12, "град"),
    M(VarName.M, 6, 60, "H*м"),
    H(VarName.H, -420, 100, "м"),
    Xa(VarName.Xa, Double.MIN_VALUE, Double.MAX_VALUE, "-"), //TODO check
    Xb(VarName.Xb, Double.MIN_VALUE, Double.MAX_VALUE, "-"),
    Ya(VarName.Ya, Double.MIN_VALUE, Double.MAX_VALUE, "-"),
    Yb(VarName.Yb, Double.MIN_VALUE, Double.MAX_VALUE, "-"),
    T(VarName.T, Double.MIN_VALUE, Double.MAX_VALUE, "с"),
    TAU(VarName.TAU, Double.MIN_VALUE, Double.MAX_VALUE, "с");

    private final ReadOnlyStringProperty name;
    private final ReadOnlyStringProperty nameWithMeasurement;
    private final String measurement;
    private final double leftValue;
    private final double rightValue;


    VariableType(String name, double leftValue, double rightValue, String meansure) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.name = new SimpleStringProperty(name);
        String measurement = "(" + meansure + ")";
        this.nameWithMeasurement = new SimpleStringProperty(name + measurement);
        this.measurement = measurement;
    }

    public boolean checkRange(double value) {
        return true; //leftValue <= value && rightValue >= value;
    }

    @Override
    public String toString() {
        return name.getValue();
    }

    public String getName() {
        return name.getValue();
    }


    public static VariableType defaultResearchVariable() {
        return Xa;
    }

    public static VariableType defaultAltVariable() {
        return RO;
    }

    public ReadOnlyStringProperty getNameWithMeasurement() {
        return nameWithMeasurement;
    }

    public static class VarName {
        public static final String RO = "\u03C1";
        public static final String R = "R";
        public static final String L = "L";
        public static final String E = "e";
        public static final String Zc = "Zc";
        public static final String GAMMA = "\u03B3";
        public static final String M = "M";
        public static final String H = "H";
        public static final String Xa = "Xa";
        public static final String Xb = "Xb";
        public static final String Ya = "Ya";
        public static final String Yb = "Yb";
        public static final String T = "t";
        public static final String TAU = "\u03C4";
        public static final String DELTA = "\u0394";
        public static final String PHI = "\u03C6";
    }

}
