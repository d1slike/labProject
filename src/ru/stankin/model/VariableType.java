package ru.stankin.model;

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

    private final String name;

    VariableType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
