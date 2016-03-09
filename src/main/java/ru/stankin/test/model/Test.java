package ru.stankin.test.model;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import jfork.nproperty.Cfg;
import ru.stankin.Configs;
import ru.stankin.test.holders.QuestionsHolder;

import java.util.List;

/**
 * Created by Dislike on 06.02.2016.
 */
public class Test {

    private List<Question> questions;
    private TIntIntMap answersMap;

    private int availableAttempts;

    private int currentQuestionCorrectAnswer;
    private int currentStudentAnswer;
    private int currentQuestionPosition;
    private List<Answer> currentAnswers;
    private int correctAnswersCount;


    public Test() {
        availableAttempts = Configs.Test.attempts();
        answersMap = new TIntIntHashMap();
        clearAndUpdateQuestions();
    }

    public void decrementAttempts() {
        availableAttempts--;
    }

    public void clearAndUpdateQuestions() {
        clear();
        questions = QuestionsHolder.getInstance().getRndListOfQuestion();
    }

    /**
     * Обновляет состояние объекта под новый вопрос и возвращает текст задания нового вопроса
     *
     * @return подготовленный вопрос
     */
    public Question prepareAndGetNextQuestion() {
        Question question = questions.get(currentQuestionPosition + 1);
        currentQuestionPosition++;
        currentAnswers = question.shakeAnswers();
        currentQuestionCorrectAnswer = question.getCorrectAnswerId();
        answersMap.clear();
        int i = 0;
        for (Answer answer : currentAnswers)
            answersMap.put(i++, answer.getId());
        return question;
    }

    public boolean checkCurrentStudentAnswer() {
        boolean correct = answersMap.get(currentStudentAnswer) == currentQuestionCorrectAnswer;
        if (correct)
            correctAnswersCount++;
        return correct;
    }

    public boolean haveMoreQuestion() {
        return currentQuestionPosition < Configs.Test.maxQuestions() - 1;
    }

    public boolean haveAnyAttempts() {
        return availableAttempts > 0;
    }

    public List<Answer> getCurrentAnswerList() {
        return currentAnswers;
    }

    public boolean isCompleteCorrect() {
        return correctAnswersCount >= Configs.Test.correctAnswersToComplete();
    }

    public int getAvailableAttempts() {
        return availableAttempts;
    }

    public int getCorrectAnswersCount() {
        return correctAnswersCount;
    }

    public int getCurrentQustionNumber() {
        return currentQuestionPosition + 1;
    }

    public void setCurrentStudentAnswer(int number) {
        currentStudentAnswer = number;
    }

    private void clear() {
        correctAnswersCount = 0;
        currentQuestionCorrectAnswer = -1;
        currentQuestionPosition = -1;
        answersMap.clear();
        currentStudentAnswer = -1;
    }
}
