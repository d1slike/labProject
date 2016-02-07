package ru.stankin.test.model;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import ru.stankin.test.holders.QuestionsHolder;

import java.util.List;

/**
 * Created by Dislike on 06.02.2016.
 */
public class Test {

    public static final int MAX_QUESTIONS = 10;
    public static final int MAX_ATTEMPTS = 2;
    public static final int MAX_MINUTES_TO_COMPLETE = 10;
    public static final int MIN_CORRECT_ANSWERS_TO_COMPLETE = 6;

    private List<Question> questions;
    private TIntIntMap answersMap;

    private int availableAttempts;

    private int currentQuestionCorrectAnswer;
    private int currentStudentAnswer;
    private int currentQuestionPosition;
    private List<Answer> currentAnswers;
    private int correctAnswersCount;


    public Test() {
        this.questions = QuestionsHolder.getInstance().getRndListOfQuestion();
        availableAttempts = MAX_ATTEMPTS;
        answersMap = new TIntIntHashMap();
        clear();
    }

    public void decrementAttempts() {
        availableAttempts--;
        if (availableAttempts > 0) {
            clear();
            questions = QuestionsHolder.getInstance().getRndListOfQuestion();
        }
    }

    private void clear() {
        correctAnswersCount = 0;
        currentQuestionCorrectAnswer = -1;
        currentQuestionPosition = -1;
        answersMap.clear();
        currentStudentAnswer = -1;
    }

    /**
     * Обновляет состояние объекта под новый вопрос и возвращает текст задания нового вопроса
     *
     * @return текст задания нового вопроса
     */
    public String prepareAndGetNextQuestion() {
        Question question = questions.get(currentQuestionPosition + 1);
        currentQuestionPosition++;
        currentAnswers = question.shakeAnswers();
        currentQuestionCorrectAnswer = question.getCorrectAnswerId();
        answersMap.clear();
        int i = 0;
        for (Answer answer : currentAnswers)
            answersMap.put(i++, answer.getId());
        return question.getText();
    }

    public void setCurrentStudentAnswer(int number) {
        currentStudentAnswer = number;
    }

    public void checkCurrentAnswer() {
        if (answersMap.get(currentStudentAnswer) == currentQuestionCorrectAnswer)
            correctAnswersCount++;
    }

    public List<Answer> getCurrentAnswerList() {
        return currentAnswers;
    }

    public boolean isCompleteCorrect() {
        return correctAnswersCount >= MIN_CORRECT_ANSWERS_TO_COMPLETE;
    }

    public int getAvailableAttempts() {
        return availableAttempts;
    }

}
