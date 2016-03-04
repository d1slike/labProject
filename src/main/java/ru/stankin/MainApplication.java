package ru.stankin;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.stankin.test.holders.QuestionsHolder;
import ru.stankin.updater.ApplicationUpdater;
import ru.stankin.utils.ImageCache;
import ru.stankin.utils.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;


/**
 * Created by DisDev on 21.01.2016.
 */
public class MainApplication extends Application {

    public static final String PROGRAM_NAME = "RCalc";
    private static Image MAIN_APP_ICON;

    private Stage primaryStage;
    private GlobalStage currentGlobalStage;
    private BorderPane root;
    private AbstractController activeController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle(PROGRAM_NAME);
        nextStage();
        primaryStage.show();
    }

    public void initMainApp() {
        try {
            File file = new File("resources/image/icon.png");
            if (file.exists())
                MAIN_APP_ICON = new Image(file.toURI().toURL().toString());
            else
                throw new FileNotFoundException();
        } catch (Exception ex) {
            Util.showProgramsFilesSpoiled();
        }

        Configs.load();
        QuestionsHolder.getInstance();
        ImageCache.getInstance();


        primaryStage.close();
        primaryStage.getIcons().add(MAIN_APP_ICON);
        nextStage();
        primaryStage.show();
    }

    public void nextStage() {
        currentGlobalStage = currentGlobalStage == null ? GlobalStage.UPDATE : currentGlobalStage.next();
        prepareUI();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static Image getIcon() {
        return MAIN_APP_ICON;
    }

    private static void main(String args[]) {
        launch(args);
    }

    private void prepareUI() {
        try {
            if (root == null) {
                root = new BorderPane();
                primaryStage.setScene(new Scene(root));
            }
            if (activeController != null) {
                activeController.prepareForNext();
                activeController = null;
            }

            URL url = getClass().getResource("/fxmls/" + currentGlobalStage.getPathToForm());
            FXMLLoader loader = new FXMLLoader(url);
            Pane pane = loader.load();
            activeController = loader.getController();
            activeController.setMainApplication(this);
            root.setCenter(pane);
            primaryStage.sizeToScene();
            if (currentGlobalStage == GlobalStage.UPDATE) {
                primaryStage.setResizable(false);
                primaryStage.setOnCloseRequest(Event::consume);
            }
        } catch (Exception ex) {
            Util.showMessageAndCloseProgram(ex);
        }
    }

    private enum GlobalStage {
        UPDATE("update.fxml"),
        WELCOME("welcome.fxml"),
        TEST("frameForTest.fxml"),
        MAIN_LAB_WORK("workFrame.fxml");

        private final String pathToForm;

        GlobalStage(String pathToForm) {
            this.pathToForm = pathToForm;
        }

        public GlobalStage next() {
            return this == MAIN_LAB_WORK ? this : values()[ordinal() + 1];
        }

        public String getPathToForm() {
            return pathToForm;
        }
    }
}
