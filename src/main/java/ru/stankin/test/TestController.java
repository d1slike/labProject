package ru.stankin.test;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.stankin.AbstractController;
import ru.stankin.MainApplication;
import ru.stankin.enums.AnswerNumber;
import ru.stankin.test.model.Answer;
import ru.stankin.test.model.Question;
import ru.stankin.test.model.Test;
import ru.stankin.utils.ImageCache;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by DisDev on 05.02.2016.
 */
public class TestController extends AbstractController {

    private static final int SECONDS_INIT_VALUE = 59;

    private static final Paint RED = Paint.valueOf(Color.RED.toString());
    private static final Paint GREEN = Paint.valueOf(Color.DARKGREEN.toString());

    private int currentMinutesToCompleteTest;
    private int secondsToShow;

    private Timeline currentTimeLine;

    private Test test;

    private State currentState;


    @FXML
    private Label attemptsLabel;
    @FXML
    private Label minutesLabel;
    @FXML
    private Label secondsLabel;
    @FXML
    private Button nextButton;

    @FXML
    private Label questionText;
    @FXML
    private ImageView imageView;

    @FXML
    private Label currentQuestion;
    @FXML
    private Label maxQuestion;

    @FXML
    private Label currentResultLabel;

    @FXML
    private RadioButton firstAnswer;
    @FXML
    private RadioButton secondAnswer;
    @FXML
    private RadioButton thirdAnswer;
    @FXML
    private RadioButton fourthAnswer;

    private List<RadioButton> allButtons;

    @Override
    public void prepareForNext() {
        if (currentTimeLine != null) {
            currentTimeLine.stop();
            currentTimeLine = null;
        }
        test = null;
        allButtons.clear();
    }

    @FXML
    private void initialize() {
        test = new Test();
        prepareUIToStartTest();
        maxQuestion.setText(Integer.toString(Test.MAX_QUESTIONS));
        currentTimeLine = new Timeline(new KeyFrame(Duration.seconds(1),
                event -> timeTick()));
        allButtons = new ArrayList<>(4);
        Stream.of(firstAnswer, secondAnswer, thirdAnswer, fourthAnswer).forEach(allButtons::add);
        currentResultLabel.setVisible(false);
        showNextQuestion();
        currentTimeLine.setCycleCount(Animation.INDEFINITE);
        currentTimeLine.play();
    }

    private synchronized void timeTick() {
        if (--secondsToShow == 0) {
            if (currentMinutesToCompleteTest == 0) {
                tryAgain();
                return;
            }
            secondsToShow = SECONDS_INIT_VALUE;
            currentMinutesToCompleteTest--;
            minutesLabel.setText(Integer.toString(currentMinutesToCompleteTest));
        }
        updateSecondsLabel();
    }

    private void tryAgain() {
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
        currentState = State.SELECT_ANSWER;
        Question question = test.prepareAndGetNextQuestion();
        imageView.setVisible(false);
        if (question.getImgSource() != null) {
            imageView.setVisible(true);
            imageView.setImage(ImageCache.getInstance().getByName(question.getImgSource()).getImage());
        }
        if (question.getText() != null)
            questionText.setText(question.getText());
        currentQuestion.setText(Integer.toString(test.getCurrentQustionNumber()));
        int answerNum = AnswerNumber.FIRST;
        allButtons.forEach(radioButton -> {
            radioButton.setGraphic(null);
            radioButton.setText("");
            radioButton.setVisible(false);
        });
        firstAnswer.fire();
        onSelectFirst();
        for (Answer answer : test.getCurrentAnswerList()) {
            String text = answer.getText();
            String imgSource = answer.getImgSource();
            switch (answerNum) {
                case AnswerNumber.FIRST:
                    if (imgSource != null)
                        firstAnswer.setGraphic(ImageCache.getInstance().getByName(imgSource));
                    if (text != null)
                        firstAnswer.setText(text);
                    firstAnswer.setVisible(true);
                    break;
                case AnswerNumber.SECOND:
                    if (imgSource != null)
                        secondAnswer.setGraphic(ImageCache.getInstance().getByName(imgSource));
                    if (text != null)
                        secondAnswer.setText(text);
                    secondAnswer.setVisible(true);
                    break;
                case AnswerNumber.THIRD:
                    if (imgSource != null)
                        thirdAnswer.setGraphic(ImageCache.getInstance().getByName(imgSource));
                    if (text != null)
                        thirdAnswer.setText(text);
                    thirdAnswer.setVisible(true);
                    break;
                case AnswerNumber.FOURTH:
                    if (imgSource != null)
                        fourthAnswer.setGraphic(ImageCache.getInstance().getByName(imgSource));
                    if (text != null)
                        fourthAnswer.setText(text);
                    fourthAnswer.setVisible(true);
                    break;
            }

            answerNum++;
        }

    }

    private void prepareUIToStartTest() {

        if (test == null)
            return;

        currentMinutesToCompleteTest = Test.MAX_MINUTES_TO_COMPLETE;
        secondsToShow = SECONDS_INIT_VALUE;

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
    private synchronized void onNextButtonClick() {
        if (test == null)
            return;
        if (currentState == State.SELECT_ANSWER) {
            currentTimeLine.pause();
            boolean isCorrect = test.checkCurrentStudentAnswer();
            currentResultLabel.setText(isCorrect ? "Верный ответ" : "Неверный ответ");
            currentResultLabel.setTextFill(isCorrect ? GREEN : RED);
            currentResultLabel.setVisible(true);
            changeStateOfKeyElementsOnPanel(false);
            currentState = State.WAIT;
        } else if (currentState == State.WAIT) {
            changeStateOfKeyElementsOnPanel(true);
            if (test.haveMoreQuestion()) {
                showNextQuestion();
                currentTimeLine.play();
            } else if (test.isCompleteCorrect())
                showResults(true);
            else if (test.haveAnyAttempts())
                tryAgain();
        }
    }

    private void changeStateOfKeyElementsOnPanel(boolean enable) {
        allButtons.forEach(radioButton -> radioButton.setDisable(!enable));
        currentResultLabel.setVisible(!enable);
        nextButton.setDefaultButton(!enable);
    }

    private void showResults(boolean success) {
        BorderPane borderPane = new BorderPane();

        borderPane.setPadding(new Insets(20));
        Stage tmpStage = new Stage();

        VBox vBox = new VBox(25);
        vBox.setAlignment(Pos.CENTER);

        Label result = new Label(success ? "Тест завершен успешно" : "Тест не пройден");
        result.setTextFill(success ? GREEN : RED);
        result.setFont(Font.font(18));
        result.setAlignment(Pos.CENTER);
        vBox.getChildren().add(result);

        Label correctAnswerCount = new Label("Верных ответов: " + test.getCorrectAnswersCount() + " / " + Test.MAX_QUESTIONS);
        correctAnswerCount.setFont(Font.font(14));
        correctAnswerCount.setAlignment(Pos.CENTER);
        vBox.getChildren().add(correctAnswerCount);

        boolean next = success || !test.haveAnyAttempts();
        Button actionButton = new Button(next ? "Приступить к лабораторной работе" : "Пройти тест заново");
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
        Stage primaryStage = getMainApplication().getPrimaryStage();
        tmpStage.initOwner(primaryStage);
        tmpStage.setIconified(false);
        tmpStage.getIcons().clear();
        tmpStage.getIcons().add(MainApplication.getIcon());
        tmpStage.initModality(Modality.APPLICATION_MODAL);
        tmpStage.setScene(new Scene(borderPane));
        tmpStage.setResizable(false);
        tmpStage.setTitle("Результаты теста");
        tmpStage.setOnCloseRequest(Event::consume);
        tmpStage.showAndWait();
    }

    public enum State {
        SELECT_ANSWER, WAIT;
    }
}
