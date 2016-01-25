package ru.stankin.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

/**
 * Created by DisDev on 25.01.2016.
 */
public class ResultRecord {
    private final ObservableValue<Double> time;
    private final ObservableValue<Double> altVar;
    private final ObservableValue<Double> staticReaction;
    private final ObservableValue<Double> dynamicReaction;
    private final ObservableValue<Double> fullReaction;

    public ResultRecord(final double timeValue,
                        final double altVarValue,
                        final double staticReactionValue,
                        final double dynamicRactionValue,
                        final double fullReactionValue)
    {
        this.time = new SimpleObjectProperty<>(timeValue);
        altVar = new SimpleObjectProperty<>(altVarValue);
        staticReaction = new SimpleObjectProperty<>(staticReactionValue);
        dynamicReaction = new SimpleObjectProperty<>(dynamicRactionValue);
        fullReaction = new SimpleObjectProperty<>(fullReactionValue);

    }


    public Double getTime() {
        return time.getValue();
    }

    public ObservableValue<Double> timeProperty() {
        return time;
    }

    public Double getAltVar() {
        return altVar.getValue();
    }

    public ObservableValue<Double> altVarProperty() {
        return altVar;
    }

    public Double getStaticReaction() {
        return staticReaction.getValue();
    }

    public ObservableValue<Double> staticReactionProperty() {
        return staticReaction;
    }

    public Double getDynamicReaction() {
        return dynamicReaction.getValue();
    }

    public ObservableValue<Double> dynamicReactionProperty() {
        return dynamicReaction;
    }

    public Double getFullReaction() {
        return fullReaction.getValue();
    }

    public ObservableValue<Double> fullReactionProperty() {
        return fullReaction;
    }
}
