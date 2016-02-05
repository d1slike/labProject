package ru.stankin.test.model;

import ru.stankin.enums.AnswerType;

/**
 * Created by DisDev on 05.02.2016.
 */
public class Answer {
    private final int id;
    private final AnswerType type;
    private final String source;

    public Answer(int id, AnswerType type, String source) {
        this.id = id;
        this.type = type;
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public AnswerType getType() {
        return type;
    }

    public String getSource() {
        return source;
    }
}

