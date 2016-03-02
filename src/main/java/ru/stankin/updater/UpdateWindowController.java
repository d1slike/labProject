package ru.stankin.updater;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.stankin.MainApplication;

/**
 * Created by DisDev on 02.03.2016.
 */
public class UpdateWindowController {

    private final Stage window;
    private final Label currentStateLabel;
    private final ProgressBar progressBar;

    public UpdateWindowController() {

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(20));
        pane.setPrefSize(400, 100);

        currentStateLabel = new Label(ApplicationUpdater.UPDATE_STATE_CHECK_NEED_UPDATE);
        progressBar = new ProgressBar(0);
        currentStateLabel.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(10, currentStateLabel, progressBar);
        vBox.setAlignment(Pos.CENTER);
        pane.setCenter(vBox);

        window = new Stage();
        window.sizeToScene();
        window.setTitle(MainApplication.PROGRAM_NAME);
        window.setScene(new Scene(pane));
    }

    public void setCurrentStateText(String text) {
        currentStateLabel.setText(text);
    }

    public void updateProgressBar(double newValue) {
        Platform.runLater(() -> progressBar.setProgress(newValue));
    }

    public void showWindow() {
        window.show();
    }

    public void closeWindow() {
        window.close();
    }
}
