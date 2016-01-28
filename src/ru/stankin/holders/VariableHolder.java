package ru.stankin.holders;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.stankin.math.Calculator;
import ru.stankin.model.ResultRecord;
import ru.stankin.model.Variable;
import ru.stankin.enums.VariableType;

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
    public static final int ALT_VAR_MAX_STEP_COUNT = 4;
    private static final int TIME_STEPS_COUNT = 10;

    private final Map<VariableType, Variable> activeVariables;
    private final ObservableList<ResultRecord> resultRecords;
    private Variable altVariable;
    private VariableType researchVariable;

    private double lastCheckedTime;
    private int samplesForAllVarChange;
    private int samplesForTime;


    public VariableHolder() {
        activeVariables = new EnumMap<>(VariableType.class);
        resultRecords = FXCollections.observableArrayList();
        Stream.of(EDITABLE_VAR_TYPES_ARRAY).forEach(type -> activeVariables.put(type, new Variable(type, 0)));
        Calculator.initVarHolder(this);
        lastCheckedTime = 0;
        samplesForTime = TIME_STEPS_COUNT;
        samplesForAllVarChange = ALT_VAR_MAX_STEP_COUNT;
    }

    public double getVarValue(VariableType type) {
        return activeVariables.get(type).getValue();
    }

    public void clear() {
        activeVariables.values().forEach(variable -> variable.setValue(0));
        altVariable = activeVariables.get(VariableType.RO);
        researchVariable = VariableType.Xa;
        resultRecords.clear();
    }

    public Collection<Variable> getAllVars() {
        return activeVariables.values();
    }

    private void updateAltVariable() {
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

    public ObservableList<ResultRecord> getResultRecords() {
        return resultRecords;
    }

    public boolean checkTime(double timeValue) {
        boolean ok =  lastCheckedTime <= timeValue && timeValue >= 40;
        if(ok)
            lastCheckedTime = timeValue;
        return ok;
    }

    public boolean calculateNextForTime(double time) {
        resultRecords.add(Calculator.calculateReactions(time));
        if (--samplesForTime == 0) {
            samplesForAllVarChange--;
            updateAltVariable();
            samplesForTime = TIME_STEPS_COUNT;
            lastCheckedTime = 0;
        }
        return samplesForAllVarChange == 0;
    }

}
