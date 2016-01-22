package ru.stankin.model;

/**
 * Created by Dislike on 22.01.2016.
 */
public class Variable implements Alterable {
    private final VariableType type;
    protected double value;
    private int step;

    public Variable(VariableType type, double value) {
        this.type = type;
        this.value = value;
        step = 0;
    }


    public String getName() {
        return type.getName();
    }

    public VariableType getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public void setStep(int step) {
        this.step = step;
    }

    @Override
    public void addStep() {
        value+=step;
    }
}
