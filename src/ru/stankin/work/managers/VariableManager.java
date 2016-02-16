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
            VariableType.GAMMA, VariableType.M, VariableType.H, VariableType.TAU};
    public static final int ALT_VAR_MAX_STEP_COUNT = 4;
    public static final int TIME_STEPS_COUNT = 10;

    private final Map<VariableType, Variable> activeVariables;
    private final ObservableList<ResultRecord> resultRecords;
    private final Calculator calculator;
    private Variable altVariable;
    private double altVarStep;
    private VariableType researchVariable;

    private int samplesForAllVarChange;
    //private int samplesForTime;

    private double currentDeltaTime;
    private double lastTime;


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
        currentDeltaTime = 0;
        lastTime = 0;
        samplesForAllVarChange = ALT_VAR_MAX_STEP_COUNT;
        resultRecords.clear();
    }

    public void setVal(VariableType type, double v) {
        activeVariables.get(type).setValue(v);
    }

    /*public boolean calculateNextForTime(double time) {
        resultRecords.add(calculator.calculateReactions(time));
        if (--samplesForTime == 0) {
            samplesForAllVarChange--;
            updateAltVariable();
            samplesForTime = TIME_STEPS_COUNT;
            lastCheckedTime = 0;
        }
        return samplesForAllVarChange == 0;
    }*/

    /*public boolean calculateAllForCurrentAltVarValue() {
        for (int i = 0; i < TIME_STEPS_COUNT; i++) {
            lastTime += currentDeltaTime;
            getResultRecords().add(calculator.calculateReactions(lastTime));
        }
        samplesForAllVarChange--;
        return samplesForAllVarChange == 0;
    }*/

    public void calculateForTau() {
        getResultRecords().add(calculator.calculateReactions(lastTime, 1));
        calculator.addToCacheOmega();
    }

    public void calculate() {
        for (int i = 0; i < ALT_VAR_MAX_STEP_COUNT; i++) {
            for (int j = 0; j < TIME_STEPS_COUNT; j++) {
                lastTime += currentDeltaTime;
                getResultRecords().add(calculator.calculateReactions(lastTime, j + 2));
            }
            if (i != ALT_VAR_MAX_STEP_COUNT - 1) {
                updateAltVariable();
                lastTime = calculator.getNextTime();
                getResultRecords().add(calculator.calculateReactions(lastTime, 1));
            }

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

    public long getRPM() {
        return calculator.calcRPM(lastTime);
    }

    public void setCurrentDeltaTime(double currentDeltaTime) {
        this.currentDeltaTime = currentDeltaTime;
    }

    public void setLastTimeToTau() {
        lastTime = getVarValue(VariableType.TAU);
    }
}
