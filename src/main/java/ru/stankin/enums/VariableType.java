package ru.stankin.enums;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import ru.stankin.utils.Util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by Dislike on 22.01.2016.
 */
public enum  VariableType {

    RO(VarName.RO, -33, 8, "Кг/м\u00B2", "density.gif"),
    R(VarName.R, -54, 44, "м", "radius.gif"),
    L(VarName.L, -300, 80, "м", "length.gif"),
    E(VarName.E, 0, 12, "м", "ext.gif"),
    Zc(VarName.Zc, 30, 285, "м", null),
    GAMMA(VarName.GAMMA, 0, 12, "град", "angle.gif"),
    M(VarName.M, 6, 60, "H*м", "moment.gif"),
    H(VarName.H, -420, 100, "м", "distance.gif"),
    Xa(VarName.Xa, Double.MIN_VALUE, Double.MAX_VALUE, "-", null), //TODO check
    Xb(VarName.Xb, Double.MIN_VALUE, Double.MAX_VALUE, "-", null),
    Ya(VarName.Ya, Double.MIN_VALUE, Double.MAX_VALUE, "-", null),
    Yb(VarName.Yb, Double.MIN_VALUE, Double.MAX_VALUE, "-", null),
    T(VarName.T, Double.MIN_VALUE, Double.MAX_VALUE, "с", null),
    TAU(VarName.TAU, Double.MIN_VALUE, Double.MAX_VALUE, "с", null),
    DELTA_T(VarName.DELTA + VarName.T, Double.MIN_VALUE, Double.MAX_VALUE, "c", null);

    private final ReadOnlyStringProperty name;
    private final ReadOnlyStringProperty nameWithMeasurement;
    private final String measurement;
    private final double leftValue;
    private final double rightValue;
    private CompletableFuture<Image> animationGif;


    VariableType(String name, double leftValue, double rightValue, String measure, String animationGif) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.name = new SimpleStringProperty(name);
        String measurement = "(" + measure + ")";
        this.nameWithMeasurement = new SimpleStringProperty(name + measurement);
        this.measurement = measurement;

        if (animationGif != null) {
            this.animationGif = Util.asyncLoadImageFromResources("gif/" + animationGif);
        } else {
            this.animationGif = CompletableFuture.completedFuture(null);
        }
    }

    public boolean checkRange(double value) {
        return true; //leftValue <= value && rightValue >= value;
    }

    public Image getAnimationGif() {
        try {
            return animationGif.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
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
