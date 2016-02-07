package ru.stankin.test;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.util.Duration;
import ru.stankin.AbstractController;
import ru.stankin.enums.AnswerNumber;
import ru.stankin.enums.AnswerType;
import ru.stankin.test.model.Answer;
import ru.stankin.test.model.Test;
import ru.stankin.utils.ImageCache;

import java.util.stream.Stream;

/**
 * Created by DisDev on 05.02.2016.
 */
public class TestController extends AbstractController {

    private int currentMinutesToCompleteTest;
    private int secondsToShow;

    private Timeline currentTimeLine;

    private Test test;


    @FXML
    private Label attemptsLabel;
    @FXML
    private Label minutesLabel;
    @FXML
    private Label secondsLabel;

    @FXML
    private Label questionText;

    @FXML
    private RadioButton firstAnswer;
    @FXML
    private RadioButton secondAnswer;
    @FXML
    private RadioButton thirdAnswer;
    @FXML
    private RadioButton fourthAnswer;

    @FXML
    private void initialize() {
        test = new Test();
        clear();
        currentTimeLine = new Timeline(new KeyFrame(Duration.seconds(1),
                event -> timeTick()));
        showNextQuestion();
        currentTimeLine.setCycleCount(Animation.INDEFINITE);
        currentTimeLine.play();

    }


    private void timeTick() {
        if (--secondsToShow == 0) {
            if (currentMinutesToCompleteTest == 0) {
                tryAgain();
                return;
            }
            secondsToShow = 59;
            currentMinutesToCompleteTest--;
            minutesLabel.setText(Integer.toString(currentMinutesToCompleteTest));
        }
        updateSecondsLabel();
    }

    private synchronized void tryAgain() {

        clear();
        test.decrementAttempts();
        if (test.getAvailableAttempts() == 0) {
            //todo перейти лабе, оставить сообщение на главном экране о том что итоговый бал снижен
            return;
        }

    }

    private void updateSecondsLabel() {
        String secInText = Integer.toString(secondsToShow);
        secondsLabel.setText(secondsToShow >= 10 ? secInText : "0" + secInText);
    }

    private void showNextQuestion() {
        String currentQuestionText = test.prepareAndGetNextQuestion();
        questionText.setText(currentQuestionText);
        int answerNum = AnswerNumber.FIRST;
        Stream.of(firstAnswer, secondAnswer, thirdAnswer, fourthAnswer).forEach(radioButton -> {
            radioButton.setGraphic(null);
            radioButton.setText("");
        });
        firstAnswer.fire();
        onSelectFirst();
        for (Answer answer : test.getCurrentAnswerList()) {
            String answerSource = answer.getSource();
            switch (answerNum) {
                case AnswerNumber.FIRST:
                    if (answer.getType() == AnswerType.IMG)
                        firstAnswer.setGraphic(ImageCache.getInstance().getByName(answerSource));
                    else
                        firstAnswer.setText(answerSource);
                    break;
                case AnswerNumber.SECOND:
                    if (answer.getType() == AnswerType.IMG)
                        secondAnswer.setGraphic(ImageCache.getInstance().getByName(answerSource));
                    else
                        secondAnswer.setText(answerSource);
                    break;
                case AnswerNumber.THIRD:
                    if (answer.getType() == AnswerType.IMG)
                        thirdAnswer.setGraphic(ImageCache.getInstance().getByName(answerSource));
                    else
                        thirdAnswer.setText(answerSource);
                    break;
                case AnswerNumber.FOURTH:
                    if (answer.getType() == AnswerType.IMG)
                        fourthAnswer.setGraphic(ImageCache.getInstance().getByName(answerSource));
                    else
                        fourthAnswer.setText(answerSource);
                    break;
            }

            answerNum++;
        }

    }

    private void clear() {

        currentMinutesToCompleteTest = Test.MAX_MINUTES_TO_COMPLETE;
        secondsToShow = 59;

        minutesLabel.setText(Integer.toString(currentMinutesToCompleteTest));
        attemptsLabel.setText(Integer.toString(test.getAvailableAttempts()));
        updateSecondsLabel();

        if (currentTimeLine != null)
            currentTimeLine.stop();
    }

    @FXML
    private void onSelectFirst() {
        test.setCurrentStudentAnswer(AnswerNumber.FIRST);
    }

    @FXML
    private void onSelectSecond() {
        test.setCurrentStudentAnswer(AnswerNumber.SECOND);
    }

    @FXML
    private void onSelectThird() {
        test.setCurrentStudentAnswer(AnswerNumber.THIRD);
    }

    @FXML
    private void onSelectFourth() {
        test.setCurrentStudentAnswer(AnswerNumber.FOURTH);
    }

    @FXML
    private void onNextButtonClick() {
        test.checkCurrentAnswer();
        showNextQuestion();
    }
}
