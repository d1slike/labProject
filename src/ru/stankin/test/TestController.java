package ru.stankin.test;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
        prepareUIToStartTest();
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
        currentTimeLine.stop();
        test.decrementAttempts();
        test.clearAndUpdateQuestions();
        showResults(false);
        prepareUIToStartTest();
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

    private void prepareUIToStartTest() {

        if(test == null)
            return;

        currentMinutesToCompleteTest = Test.MAX_MINUTES_TO_COMPLETE;
        secondsToShow = 59;

        minutesLabel.setText(Integer.toString(currentMinutesToCompleteTest));
        attemptsLabel.setText(Integer.toString(test.getAvailableAttempts()));
        updateSecondsLabel();

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
        if (test == null)
            return;
        test.checkCurrentAnswer();
        if (test.haveMoreQuestion())
            showNextQuestion();
        else if (test.isCompleteCorrect())
            showResults(true);
        else if (test.haveAnyAttempts())
            tryAgain();
    }

    private void showResults(boolean success) {
        BorderPane borderPane = new BorderPane();

        borderPane.setPadding(new Insets(20));
        Stage tmpStage = new Stage();

        VBox vBox = new VBox(25);
        vBox.setAlignment(Pos.CENTER);

        Label result = new Label(success ? "Тест завершен успешно" : "Тест не пройден");
        Color textColor = success ? Color.GREEN : Color.RED;
        result.setTextFill(Paint.valueOf(textColor.toString()));
        result.setFont(Font.font(18));
        result.setAlignment(Pos.CENTER);
        vBox.getChildren().add(result);

        Label correctAnswerCount = new Label(test.getCorrectAnswersCount() + " / " + Test.MAX_QUESTIONS);
        correctAnswerCount.setFont(Font.font(14));
        correctAnswerCount.setAlignment(Pos.CENTER);
        vBox.getChildren().add(correctAnswerCount);

        boolean next = success || !test.haveAnyAttempts();
        Button actionButton = new Button(next ? "Приступить лабораторной работе" : "Пройти тест заново");
        actionButton.setPrefSize(200, 30);
        EventHandler<ActionEvent> eventHandler;
        if (next)
            eventHandler = event -> {
                tmpStage.close();
                getMainApplication().nextStage();
            };
        else
            eventHandler = event -> {
                tmpStage.close();
                showNextQuestion();
                currentTimeLine.play();
            };
        actionButton.setOnAction(eventHandler);
        vBox.getChildren().add(actionButton);

        borderPane.setCenter(vBox);
        BorderPane.setAlignment(vBox, Pos.CENTER);
        tmpStage.initOwner(getMainApplication().getPrimaryStage());
        tmpStage.initModality(Modality.APPLICATION_MODAL);
        tmpStage.setScene(new Scene(borderPane));
        tmpStage.setResizable(false);
        tmpStage.setTitle("Результат теста");
        tmpStage.setOnCloseRequest(event -> {
            tmpStage.close();
            tmpStage.showAndWait();
        });
        tmpStage.showAndWait();
    }

    @Override
    public void prepareForNext() {
        if (currentTimeLine != null) {
            currentTimeLine.stop();
            currentTimeLine = null;
        }
        test = null;
    }
}
