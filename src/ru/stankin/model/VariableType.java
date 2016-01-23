package ru.stankin.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;

/**
 * Created by Dislike on 22.01.2016.
 */
public enum  VariableType {

    RO("RO"),
    R("R"),
    L("L"),
    E("E"),
    Zc("Zc"),
    GAMMA("GAMMA"),
    M("M"),
    H("H");

    private final ObservableValue<String> name;

    VariableType(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.getValue();
    }

    public ObservableValue<String> getNameProperty() {
        return name;
    }
}
