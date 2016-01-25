package ru.stankin.holders;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableListValue;
import ru.stankin.model.ResultRecord;
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
    public static final VariableType[] EDITABLE_VAR_TYPES_ARRAY = {VariableType.RO, VariableType.R, VariableType.L, VariableType.E, VariableType.Zc,
            VariableType.GAMMA, VariableType.M, VariableType.H};

    private final Map<VariableType, Variable> activeVariables;
    private final ObservableListValue<ResultRecord> resultRecords;
    private Variable altVariable;
    private VariableType researchVariable;


    public VariableHolder() {
        activeVariables = new EnumMap<>(VariableType.class);
        resultRecords = new SimpleListProperty<>();
        Stream.of(EDITABLE_VAR_TYPES_ARRAY).forEach(type -> activeVariables.put(type, new Variable(type, 0)));
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
        researchVariable = VariableType.Xa;
        resultRecords.clear();
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

    public void setResearchVariableType(VariableType type) {
        researchVariable = type;
    }

    public VariableType getResearchVariable() {
        return researchVariable;
    }

    public ObservableListValue<ResultRecord> getResultRecords() {
        return resultRecords;
    }

}
