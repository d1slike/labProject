package ru.stankin.updater;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import ru.stankin.AbstractController;

/**
 * Created by DisDev on 02.03.2016.
 */
public class UpdateWindowController extends AbstractController {

    @FXML
    private Label currentStateLabel;
    @FXML
    private ProgressBar progressBar;

    private ApplicationUpdater updater;

    public UpdateWindowController() {

    }

    @FXML
    private void initialize() {
        updater = new ApplicationUpdater(this);
        updater.setOnSucceeded(event -> getMainApplication().initMainApp());
        currentStateLabel.setText(ApplicationUpdater.UPDATE_STATE_CHECK_NEED_UPDATE);
        progressBar.progressProperty().bind(updater.progressProperty());
        updater.start();

    }

    @Override
    public void prepareForNext() {
        updater.terminate();
        updater = null;
        Stage primaryStage = getMainApplication().getPrimaryStage();
        primaryStage.setResizable(true);
        primaryStage.setOnCloseRequest(null);
    }

    public void setCurrentStateText(String text) {
        Platform.runLater(() -> currentStateLabel.setText(text));
    }


}
