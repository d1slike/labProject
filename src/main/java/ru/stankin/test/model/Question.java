package ru.stankin.test.model;

import ru.stankin.utils.Rnd;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by DisDev on 05.02.2016.
 */
public class Question {
    private final int id;
    private final String imgSource;
    private final String text;
    private final int correctAnswerId;
    private final Map<Integer, Answer> answers;

    public Question(int id, String imgSource, String text, int correctAnswerId, Map<Integer, Answer> answers) {
        this.id = id;
        this.imgSource = imgSource;
        this.text = text;
        this.correctAnswerId = correctAnswerId;
        this.answers = answers;
    }

    public List<Answer> shakeAnswers() {
        List<Answer> copy = new LinkedList<>(answers.values());
        List<Answer> toReturn = new ArrayList<>(answers.size());
        while (!copy.isEmpty()) {
            int copySize = copy.size();
            Answer answer = copySize == 1 ? copy.remove(0) : copy.remove(Rnd.get(copySize));
            toReturn.add(answer);
        }

        return toReturn;

    }

    public String getText() {
        return text;
    }

    public int getCorrectAnswerId() {
        return correctAnswerId;
    }

    public String getImgSource() {
        return imgSource;
    }
}
