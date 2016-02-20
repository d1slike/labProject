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
    public static final VariableType[] EDITABLE_VAR_TYPES_ARRAY = {VariableType.RO, VariableType.R, VariableType.L, VariableType.E, /*VariableType.Zc,*/
            VariableType.GAMMA, VariableType.M, VariableType.H, VariableType.TAU};
    public static final int ALT_VAR_MAX_STEP_COUNT = 4;
    public static final int TIME_STEPS_COUNT = 11;

    private final Map<VariableType, Variable> activeVariables;
    private final ObservableList<ResultRecord> resultRecords;
    private Calculator calculator;
    private Variable altVariable;
    private double altVarStep;
    private VariableType researchVariable;


    private double currentDeltaTime;
    private double lastTime;


    public VariableManager() {
        activeVariables = new EnumMap<>(VariableType.class);
        resultRecords = FXCollections.observableArrayList();
        Stream.of(EDITABLE_VAR_TYPES_ARRAY).forEach(type -> activeVariables.put(type, new Variable(type, 0)));
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
        currentDeltaTime = 0;
        lastTime = 0;
        resultRecords.clear();
        calculator = new Calculator(this);
    }

    public void setVal(VariableType type, double v) {
        activeVariables.get(type).setValue(v);
    }

    public double calculatePhiForTau() {
        return calculator.getPhiInDegrees(getVarValue(VariableType.TAU));
    }

    public void calculateAllReactions() {
        lastTime = getVarValue(VariableType.TAU);
        calculator.initOmegaForNextTimeCalculate();
        for (int i = 0; i < ALT_VAR_MAX_STEP_COUNT; i++) {
            for (int j = 0; j < TIME_STEPS_COUNT; j++) {
                final ResultRecord resultRecord = calculator.calculateReactions(lastTime);
                resultRecord.setIndexAndPointNumber(resultRecords.size(), j + 1);
                getResultRecords().add(resultRecord);
                lastTime += currentDeltaTime;
            }
            updateAltVariable();
            lastTime = calculator.getNextTime();
        }

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

    public void setAltVarStep(double altVarStep) {
        this.altVarStep = altVarStep;
    }

    public void updateAltVariable() {
        altVariable.setValue(altVariable.getValue() + altVarStep);
    }

    public long calculateRPMForTau() {
        return calculator.calcRPMForTau();
    }

    public void setCurrentDeltaTime(double currentDeltaTime) {
        this.currentDeltaTime = currentDeltaTime;
    }

}
