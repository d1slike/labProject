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
    T(VarName.T, Double.MIN_VALUE, Double.MAX_VALUE, "-");

    private final ReadOnlyStringProperty name;
    private final ReadOnlyStringProperty nameWithMeansurement;
    private final double leftValue;
    private final double rightValue;


    VariableType(String name, double leftValue, double rightValue, String meansure) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.name = new SimpleStringProperty(name);
        this.nameWithMeansurement = new SimpleStringProperty(name + "(" + meansure + ")");
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

    public ReadOnlyStringProperty getNameProperty() {
        return name;
    }

    public ReadOnlyStringProperty getNameWithMeansurement() {
        return nameWithMeansurement;
    }

    private static class VarName {
        private static final String RO = "\u03C1";
        private static final String R = "R";
        private static final String L = "L";
        private static final String E = "e";
        private static final String Zc = "Zc";
        private static final String GAMMA = "\u03B3";
        private static final String M = "M";
        private static final String H = "H";
        private static final String Xa = "Xa";
        private static final String Xb = "Xb";
        private static final String Ya = "Ya";
        private static final String Yb = "Yb";
        private static final String T = "t";
    }
}
