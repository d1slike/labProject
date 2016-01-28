package ru.stankin.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableDoubleValue;
import ru.stankin.enums.VariableType;

/**
 * Created by Dislike on 22.01.2016.
 */
public final class Variable {
    private final VariableType type;
    private ObservableDoubleValue value;
    private int step;

    public Variable(VariableType type, double value) {
        this.type = type;
        this.value = new SimpleDoubleProperty(value);
        step = 0;
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

    public ObservableDoubleValue getValuePropertie() {
        return value;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void addStep() {
        double old = getValue();
        value = new SimpleDoubleProperty(old + step);
    }
}
