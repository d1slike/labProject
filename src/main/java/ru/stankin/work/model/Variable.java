package ru.stankin.work.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableDoubleValue;
import ru.stankin.enums.VariableType;

/**
 * Created by Dislike on 22.01.2016.
 */
public final class Variable {
    private final VariableType type;
    private ObservableDoubleValue value;

    public Variable(VariableType type, double value) {
        this.type = type;
        this.value = new SimpleDoubleProperty(value);
    }

    public void setValue(double newValue) {
        value = new SimpleDoubleProperty(newValue);
    }

    public String getName() {
        return type.getName();
    }

    public VariableType getType() {
        return type;
    }

    public double getValue() {
        return value.doubleValue();
    }

    public ObservableDoubleValue getValueProperties() {
        return value;
    }
}
