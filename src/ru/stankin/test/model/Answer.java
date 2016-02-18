package ru.stankin.test.model;

/**
 * Created by DisDev on 05.02.2016.
 */
public class Answer {
    private final int id;
    private final String imgSource;
    private final String text;

    public Answer(int id, String imgSource, String source) {
        this.id = id;
        this.imgSource = imgSource;
        this.text = source;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getImgSource() {
        return imgSource;
    }
}

