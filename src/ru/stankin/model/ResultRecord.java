package ru.stankin.model;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

/**
 * Created by DisDev on 25.01.2016.
 */
public class ResultRecord {
    private final ReadOnlyDoubleProperty time;
    private final ReadOnlyDoubleProperty altVar;
    private final ReadOnlyDoubleProperty staticReaction;
    private final ReadOnlyDoubleProperty dynamicReaction;
    private final ReadOnlyDoubleProperty fullReaction;
    private final ReadOnlyDoubleProperty revPerMinute;

    public ResultRecord(final double timeValue,
                        final double altVarValue,
                        final double staticReactionValue,
                        final double dynamicRactionValue,
                        final double fullReactionValue,
                        final double revPerMinuteValue)
    {
        time = new SimpleDoubleProperty(timeValue);
        altVar = new SimpleDoubleProperty(altVarValue);
        staticReaction = new SimpleDoubleProperty(staticReactionValue);
        dynamicReaction = new SimpleDoubleProperty(dynamicRactionValue);
        fullReaction = new SimpleDoubleProperty(fullReactionValue);
        revPerMinute = new SimpleDoubleProperty(revPerMinuteValue);
    }


    public double getTime() {
        return time.getValue();
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

    public double getStaticReaction() {
        return staticReaction.getValue();
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

    public ReadOnlyDoubleProperty getRPM() {
        return revPerMinute;
    }
}
