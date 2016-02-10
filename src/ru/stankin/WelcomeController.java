package ru.stankin;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.util.Duration;

/**
 * Created by DisDev on 05.02.2016.
 */
public class WelcomeController extends AbstractController {

    @FXML
    private Button nextButton;

    @FXML
    private void initialize() {
        nextButton.setDisable(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5),
                event -> nextButton.setDisable(false)));
        timeline.play();
    }

    @FXML
    private void onNextButtonClick() {
        getMainApplication().nextStage();
    }

    @Override
    public void prepareForNext() {

    }
}
