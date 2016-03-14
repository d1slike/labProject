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

    private static final String INFO_TEMPLATE = "Перед тем как присутпить к эксперементальной части работы" +
            " Вам необходимо пройти тест." +
            " Тест состоит из %qCount% вопросов, с 3-4 вариантами ответов." +
            " Максимальный бал за тест – %maxPoints%." +
            " Минимальный бал для прохождения теста %minPoints%(%minQCount% верных ответов)." +
            " На выполнение теста дается %attemptsCount% попытки, на каждую попытку дается %minutesToComplete% минут." +
            " Если с первой попытки Вам не удается набрать минимальное количество баллов, то Вам необходимо пройти его повторно." +
            " Если со второй попытке тест пройти не удается, то Вы получаете проходной бал и приступаете к выполнению второй части работы." +
            " Если Вы с первой попытки перешли порог, но не набрали желаемый балл, Вы можете пройти его повторно, сохраняя свой текущий результат, или имеете право приступить к выполнению опыта.";

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
