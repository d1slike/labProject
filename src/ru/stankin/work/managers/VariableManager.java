package ru.stankin.work.managers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.stankin.enums.VariableType;
import ru.stankin.work.math.Calculator;
import ru.stankin.work.model.ResultRecord;
import ru.stankin.work.model.Variable;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by Dislike on 22.01.2016.
 */
public class VariableManager {
    public static final VariableType[] EDITABLE_VAR_TYPES_ARRAY = {VariableType.RO, VariableType.R, VariableType.L, VariableType.E, VariableType.Zc,
            VariableType.GAMMA, VariableType.M, VariableType.H};
    public static final int ALT_VAR_MAX_STEP_COUNT = 4;
    private static final int TIME_STEPS_COUNT = 10;

    private final Map<VariableType, Variable> activeVariables;
    private final ObservableList<ResultRecord> resultRecords;
    private final Calculator calculator;
    private Variable altVariable;
    private int altVarStep;
    private VariableType researchVariable;

    private double lastCheckedTime;
    private int samplesForAllVarChange;
    private int samplesForTime;




    public VariableManager() {
        activeVariables = new EnumMap<>(VariableType.class);
        resultRecords = FXCollections.observableArrayList();
        Stream.of(EDITABLE_VAR_TYPES_ARRAY).forEach(type -> activeVariables.put(type, new Variable(type, 0)));
        calculator = new Calculator(this);
        clear();
    }

    public double getVarValue(VariableType type) {
        return activeVariables.get(type).getValue();
    }

    public void clear() {
        activeVariables.values().forEach(variable -> variable.setValue(0));
        altVariable = activeVariables.get(VariableType.RO);
        altVarStep = 0;
        researchVariable = VariableType.Xa;
        lastCheckedTime = 0;
        samplesForTime = TIME_STEPS_COUNT;
        samplesForAllVarChange = ALT_VAR_MAX_STEP_COUNT;
        resultRecords.clear();
    }

    public boolean checkTime(double timeValue) {
        boolean ok = lastCheckedTime <= timeValue && timeValue >= 40;
        if (ok)
            lastCheckedTime = timeValue;
        return ok;
    }

    public void setVal(VariableType type, double v) {
        activeVariables.get(type).setValue(v);
    }

    public boolean calculateNextForTime(double time) {
        resultRecords.add(calculator.calculateReactions(time));
        if (--samplesForTime == 0) {
            samplesForAllVarChange--;
            updateAltVariable();
            samplesForTime = TIME_STEPS_COUNT;
            lastCheckedTime = 0;
        }
        return samplesForAllVarChange == 0;
    }

    public Collection<Variable> getAllVars() {
        return activeVariables.values();
    }

    public Variable getAltVariable() {
        return altVariable;
    }

    public void setAltVariable(VariableType type) {
        altVariable = activeVariables.get(type);
    }

    public VariableType getResearchVariable() {
        return researchVariable;
    }

    public ObservableList<ResultRecord> getResultRecords() {
        return resultRecords;
    }

    public void setResearchVariableType(VariableType type) {
        researchVariable = type;
    }

    public void setAltVarStep(int altVarStep) {
        this.altVarStep = altVarStep;
    }

    private void updateAltVariable() {
        altVariable.setValue(altVariable.getValue() + altVarStep);
    }
}
