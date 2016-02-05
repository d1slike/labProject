package ru.stankin.test.model;

import gnu.trove.map.TIntObjectMap;
import ru.stankin.utils.Rnd;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by DisDev on 05.02.2016.
 */
public class Question {
    private final int id;
    private final String text;
    private final int correctAnswerId;
    private final TIntObjectMap<Answer> answers;

    public Question(int id, String text, int correctAnswerId, TIntObjectMap<Answer> answers) {
        this.id = id;
        this.text = text;
        this.correctAnswerId = correctAnswerId;
        this.answers = answers;
    }

    public List<Answer> shakeAnswers() {
        List<Answer> copy = new LinkedList<>(answers.valueCollection());
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
}
