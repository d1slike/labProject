package ru.stankin.test;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import ru.stankin.AbstractController;

/**
 * Created by DisDev on 05.02.2016.
 */
public class TestController extends AbstractController {
    public static final int MAX_QUESTIONS = 10;
    private static final int MAX_ATTEMPTS = 2;
    private static final int MAX_MINUTES_TO_COMPLETE = 10;

    private int currentAttemptsToCompleteTest;
    private int currentMinutesToCompleteTest;
    private int secondsToShow;

    private Timeline currentTimeLine;

    @FXML
    private void initialize() {
        currentAttemptsToCompleteTest = MAX_ATTEMPTS;
        currentMinutesToCompleteTest = MAX_MINUTES_TO_COMPLETE;
        secondsToShow = 59;

        minutesLabel.setText(Integer.toString(currentMinutesToCompleteTest));
        secondsLabel.setText(Integer.toString(secondsToShow));
        attemptsLabel.setText(Integer.toString(currentAttemptsToCompleteTest));

        currentTimeLine = new Timeline(new KeyFrame(Duration.seconds(1),
                event -> timeTick()));
        currentTimeLine.setCycleCount(Animation.INDEFINITE);
        currentTimeLine.play();

    }

    @FXML
    private Label attemptsLabel;
    @FXML
    private Label minutesLabel;
    @FXML
    private Label secondsLabel;

    private void timeTick() {
        if (--secondsToShow == 0) {
            if (currentMinutesToCompleteTest == 0)
                tryAgain();
            secondsToShow = 59;
            currentMinutesToCompleteTest--;
            minutesLabel.setText(Integer.toString(secondsToShow));
        }
        secondsLabel.setText(Integer.toString(secondsToShow));
    }

    private void tryAgain() {

    }
}
