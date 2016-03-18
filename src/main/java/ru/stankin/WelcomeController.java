package ru.stankin;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Created by DisDev on 05.02.2016.
 */
public class WelcomeController extends AbstractController {

    private static final String INFO_TEMPLATE = "Тест состоит из 15 вопросов, с 3-4 вариантами ответов. " +
            "Максимальный балл за тест – %maxPoints%. " +
            "Минимальный балл для прохождения теста %minPoints%(%minQCount% верных ответов). " +
            "Для выполнения теста на высокую оценку предлагается %attemptsCount% попытки, общее количество попыток неограничено, " +
            "на каждую попытку дается %minutesToComplete% минут. " +
            "Итоговый балл – максимальный результат, который получил студент за первые %attemptsCount% попытки. " +
            "Если тест удается выполнить за большее количество попыток чем %attemptsCount%, то выставляется минимальный балл.";

    @FXML
    private Button nextButton;
    @FXML
    private Label infoLabel;

    @Override
    public void prepareForNext() {
    }

    @FXML
    private void initialize() {
        infoLabel.setText(buildInfo());
        nextButton.setDisable(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5),
                event -> nextButton.setDisable(false)));
        timeline.play();
    }

    private String buildInfo() {
        String info = INFO_TEMPLATE;
        info = info.replace("%qCount%", Configs.Test.maxQuestions() + "")
                .replace("%maxPoints%", Configs.Test.maxPoints() + "")
                .replace("%minPoints%", Configs.Test.minPoints() + "")
                .replace("%minQCount%", Configs.Test.correctAnswersToComplete() + "")
                .replace("%attemptsCount%", Configs.Test.attempts() + "")
                .replace("%minutesToComplete%", Configs.Test.minutesToComplete() + "");
        return info;
    }

    @FXML
    private void onNextButtonClick() {
        getMainApplication().nextStage();
    }
}
