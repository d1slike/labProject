package ru.stankin.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

/**
 * Created by Dislike on 22.01.2016.
 */
public class Variable implements Alterable {
    private final VariableType type;
    protected ObservableValue<Double> value;
    private int step;

    public Variable(VariableType type, double value) {
        this.type = type;
        this.value = new SimpleObjectProperty<>(0.);
        step = 0;
    }

    public void setValue(double newValue) {
        value = new SimpleObjectProperty<>(newValue);
    }

    public String getName() {
        return type.getName();
    }

    public VariableType getType() {
        return type;
    }

    public double getValue() {
        return value.getValue();
    }

    public ObservableValue<Double> getValuePropertie() {
        return value;
    }


    @Override
    public void setStep(int step) {
        this.step = step;
    }

    @Override
    public void addStep() {
        double old = getValue();
        value = new SimpleObjectProperty<>(old + step);
    }
}
