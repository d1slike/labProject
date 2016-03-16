package ru.stankin.test;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
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
import ru.stankin.Configs;
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
    private Label infoAboutNextLabel;

    @FXML
    private Label questionText;
    @FXML
    private ImageView imageView;

    @FXML
    private Label currentQuestion;
    @FXML
    private Label maxQuestion;

    @FXML
    private Label currentAnswerResultLabel;

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
        allButtons.clear();
        Stage stage = getMainApplication().getPrimaryStage();
        stage.setTitle(stage.getTitle() + " - Оценка за тест: " + test.getMaxPoints());
        test = null;
    }

    @FXML
    private void initialize() {
        test = new Test();
        prepareUIToStartTest();
        maxQuestion.setText(Integer.toString(Configs.Test.maxQuestions()));
        currentTimeLine = new Timeline(new KeyFrame(Duration.seconds(1),
                event -> timeTick()));
        allButtons = new ArrayList<>(4);
        Stream.of(firstAnswer, secondAnswer, thirdAnswer, fourthAnswer).forEach(allButtons::add);
        currentAnswerResultLabel.setVisible(false);
        showNextQuestion();
        infoAboutNextLabel.setVisible(false);
        currentTimeLine.setCycleCount(Animation.INDEFINITE);
        currentTimeLine.play();
    }

    private synchronized void timeTick() {
        if (--secondsToShow == 0) {
            if (currentMinutesToCompleteTest == 0) {
                //tryAgain();
                prepareAndShowResults();
                return;
            }
            secondsToShow = SECONDS_INIT_VALUE;
            currentMinutesToCompleteTest--;
            minutesLabel.setText(Integer.toString(currentMinutesToCompleteTest));
        }
        updateSecondsLabel();
    }

    private void tryAgain() {
        //currentTimeLine.stop();
        //test.decrementAttempts();
        test.clearAndUpdateQuestions();
        prepareUIToStartTest();
        showNextQuestion();
        currentTimeLine.play();
    }

    private void prepareAndShowResults() {
        currentTimeLine.stop();
        test.decrementAttempts();
        showResults(test.isCompleteCorrectNow());
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

        currentMinutesToCompleteTest = Configs.Test.minutesToComplete() - 1;
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
            currentAnswerResultLabel.setText(isCorrect ? "Верный ответ" : "Неверный ответ");
            currentAnswerResultLabel.setTextFill(isCorrect ? GREEN : RED);
            currentAnswerResultLabel.setVisible(true);
            changeStateOfKeyElementsOnPanel(false);
            currentState = State.WAIT;
        } else if (currentState == State.WAIT) {
            changeStateOfKeyElementsOnPanel(true);
            if (test.haveMoreQuestion()) {
                showNextQuestion();
                currentTimeLine.play();
            } else {
                prepareAndShowResults();
            }

        }
    }

    private void changeStateOfKeyElementsOnPanel(boolean enable) {
        allButtons.forEach(radioButton -> radioButton.setDisable(!enable));
        currentAnswerResultLabel.setVisible(!enable);
        infoAboutNextLabel.setVisible(!enable);
        //nextButton.setDefaultButton(!enable);
    }

    private void showResults(boolean success) {
        BorderPane borderPane = new BorderPane();

        borderPane.setPadding(new Insets(20));
        Stage tmpStage = new Stage();

        VBox vBox = new VBox(25);
        vBox.setAlignment(Pos.CENTER);

        Label result = new Label(success ? "Тест выполнен успешно" : "Тест не пройден");
        result.setTextFill(success ? GREEN : RED);
        result.setFont(Font.font(18));
        result.setAlignment(Pos.CENTER);
        vBox.getChildren().add(result);

        Label correctAnswerCount = new Label("Верных ответов: " + test.getCorrectAnswersCount() + " / " + Configs.Test.maxQuestions());
        Label currentPoints = new Label("Текущий балл: " + test.getCurrentPoints());
        Label maxPoints = new Label("Максимальный набранный балл: " + test.getMaxPoints());

        Stream.of(correctAnswerCount, currentPoints, maxPoints).forEach(label -> {
            label.setFont(Font.font(14));
            label.setAlignment(Pos.CENTER);
            vBox.getChildren().add(label);
        });

        if (test.isDone()) {
            Button nextButton = new Button("Приступить к лабораторной работе");
            Label info = new Label("*Приступая к лабораторной работе, Вы получаете максимальный балл.");
            info.setFont(Font.font(14));
            info.setTextFill(Paint.valueOf(Color.GRAY.toString()));
            nextButton.setOnAction(event -> {
                tmpStage.close();
                getMainApplication().nextStage();
            });
            vBox.getChildren().addAll(info, nextButton);
        }

        if ((test.isDone() && test.haveAnyAttempts()) || (!test.isDone() && !success)) {
            Button tryAgainButton = new Button("Пройти тест повторно");
            tryAgainButton.setOnAction(event -> {
                tmpStage.close();
                tryAgain();
            });
            vBox.getChildren().add(tryAgainButton);
        }

        borderPane.setCenter(vBox);
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
