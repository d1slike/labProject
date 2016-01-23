package ru.stankin.holders;

import ru.stankin.model.Alterable;
import ru.stankin.model.Variable;
import ru.stankin.model.VariableType;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by Dislike on 22.01.2016.
 */
public class VariableHolder {

    private final Map<VariableType, Variable> activeVariables;
    private Alterable altVariable;

    public VariableHolder() {
        activeVariables = new EnumMap<>(VariableType.class);
        Stream.of(VariableType.values()).forEach(type -> activeVariables.put(type, new Variable(type, 0)));
        altVariable = activeVariables.get(VariableType.RO); //default
    }

    public double getVarValue(VariableType type) {
        return activeVariables.get(type).getValue();
    }

    public void setVarValue(VariableType type, double newValue) {
        activeVariables.get(type).setValue(newValue);
    }

    public void reset() {
        activeVariables.values().forEach(variable -> variable.setValue(0));
        altVariable = activeVariables.get(VariableType.RO);
    }

    public Collection<Variable> getAllVars() {
        return activeVariables.values();
    }

    public void updateAlterVariable() {
        altVariable.addStep();
    }

    public Variable getAltVariable() {
        return (Variable) altVariable;
    }

    public void setAltVariable(VariableType type) {
        altVariable = activeVariables.get(type);
    }
}
