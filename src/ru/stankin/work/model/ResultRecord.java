package ru.stankin.work.model;

import javafx.beans.property.*;

/**
 * Created by DisDev on 25.01.2016.
 */
public class ResultRecord {
    private final ReadOnlyIntegerProperty number;
    private final ReadOnlyDoubleProperty time;
    private final ReadOnlyDoubleProperty altVar;
    private final ReadOnlyDoubleProperty staticReaction;
    private final ReadOnlyDoubleProperty dynamicReaction;
    private final ReadOnlyDoubleProperty fullReaction;
    private final ReadOnlyDoubleProperty phiInDegrees;

    public ResultRecord(int number, final double timeValue,
                        final double altVarValue,
                        final double staticReactionValue,
                        final double dynamicRactionValue,
                        final double fullReactionValue,
                        final double phiInDegreesValue)
    {
        this.number = new SimpleIntegerProperty(number);
        time = new SimpleDoubleProperty(timeValue);
        altVar = new SimpleDoubleProperty(altVarValue);
        staticReaction = new SimpleDoubleProperty(staticReactionValue);
        dynamicReaction = new SimpleDoubleProperty(dynamicRactionValue);
        fullReaction = new SimpleDoubleProperty(fullReactionValue);
        phiInDegrees = new SimpleDoubleProperty(phiInDegreesValue);
    }

    public ReadOnlyDoubleProperty timeProperty() {
        return time;
    }

    public double getAltVar() {
        return altVar.getValue();
    }

    public ReadOnlyDoubleProperty altVarProperty() {
        return altVar;
    }

    public ReadOnlyDoubleProperty staticReactionProperty() {
        return staticReaction;
    }

    public double getDynamicReaction() {
        return dynamicReaction.getValue();
    }

    public ReadOnlyDoubleProperty dynamicReactionProperty() {
        return dynamicReaction;
    }

    public double getFullReaction() {
        return fullReaction.getValue();
    }

    public ReadOnlyDoubleProperty fullReactionProperty() {
        return fullReaction;
    }

    public ReadOnlyDoubleProperty phiInDegreesProperty() {
        return phiInDegrees;
    }

    public int getNumber() {
        return number.intValue();
    }
}
