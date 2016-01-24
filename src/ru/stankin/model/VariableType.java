package ru.stankin.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

/**
 * Created by Dislike on 22.01.2016.
 */
public enum  VariableType {

    RO("RO", -33, 8),
    R("R", -54, 44),
    L("L", -300, 80),
    E("E", 0, 12),
    Zc("Zc", 30, 285),
    GAMMA("GAMMA", 0, 12),
    M("M", 6, 60),
    H("H", -420, 100),
    Xa("Xa", Double.MIN_VALUE, Double.MAX_VALUE),
    Xb("Xb", Double.MIN_VALUE, Double.MAX_VALUE),
    Ya("Ya", Double.MIN_VALUE, Double.MAX_VALUE),
    Yb("Yb", Double.MIN_VALUE, Double.MAX_VALUE),
    T("T", Double.MIN_VALUE, Double.MAX_VALUE);

    private final ObservableValue<String> name;
    private final double leftValue;
    private final double rightValue;

    VariableType(String name, double leftValue, double rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.getValue();
    }

    public boolean checkRange(double value) {
        return leftValue <= value && rightValue >= value;
    }

    public ObservableValue<String> getNameProperty() {
        return name;
    }
}
