package ru.stankin.enums;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Dislike on 22.01.2016.
 */
public enum  VariableType {

    RO("RO", -33, 8, "Кг/м^3"),
    R("R", -54, 44, "м"),
    L("L", -300, 80, "м"),
    E("E", 0, 12, "м"),
    Zc("Zc", 30, 285, "м"),
    GAMMA("GAMMA", 0, 12, "град"),
    M("M", 6, 60, "H * м"),
    H("H", -420, 100, "м"),
    Xa("Xa", Double.MIN_VALUE, Double.MAX_VALUE, "-"), //TODO check
    Xb("Xb", Double.MIN_VALUE, Double.MAX_VALUE, "-"),
    Ya("Ya", Double.MIN_VALUE, Double.MAX_VALUE, "-"),
    Yb("Yb", Double.MIN_VALUE, Double.MAX_VALUE, "-"),
    T("T", Double.MIN_VALUE, Double.MAX_VALUE, "-");

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

    public String getName() {
        return name.getValue();
    }

    public boolean checkRange(double value) {
        return true; //leftValue <= value && rightValue >= value;
    }

    public ReadOnlyStringProperty getNameProperty() {
        return name;
    }

    public ReadOnlyStringProperty getNameWithMeansurement() {
        return nameWithMeansurement;
    }
}
