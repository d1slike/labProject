package ru.stankin;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.stankin.utils.Util;

import java.io.IOException;

/**
 * Created by DisDev on 05.02.2016.
 */
public abstract class AbstractController {
    private MainApplication mainApplication;

    public final MainApplication getMainApplication() {
        return mainApplication;
    }

    public final void setMainApplication(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
    }

    public final void showAboutDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.getFXMLUrl("about.fxml"));
            Pane pane = loader.load();

            Scene scene = new Scene(pane);

            Stage childStage = mainApplication.newChildStage("О программе");
            childStage.setScene(scene);
            childStage.centerOnScreen();
            childStage.sizeToScene();
            childStage.showAndWait();
        } catch (IOException e) {
            Util.showProgramsFilesSpoiled();
        }
    }

    public abstract void prepareForNext();

}
