package ru.stankin;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.stankin.test.holders.QuestionsHolder;
import ru.stankin.utils.ImageCache;
import ru.stankin.utils.Util;

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

    public static Image getIcon() {
        return MAIN_APP_ICON;
    }

    private static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle(PROGRAM_NAME);
        nextStage();
    }

    public void initMainApp() {
        try {
            MAIN_APP_ICON = new Image(getClass().getResource("/icon.png").toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            Util.showProgramsFilesSpoiled();
        }

        Configs.load();//load configs
        QuestionsHolder.getInstance();
        ImageCache.getInstance();


        primaryStage.getIcons().add(MAIN_APP_ICON);
        nextStage();
    }

    public void nextStage() {
        currentGlobalStage = currentGlobalStage == null ? GlobalStage.UPDATE : currentGlobalStage.next();
        prepareUI();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
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

            URL url = getClass().getResource("/" + currentGlobalStage.getPathToForm());
            FXMLLoader loader = new FXMLLoader(url);
            Pane pane = loader.load();
            activeController = loader.getController();
            activeController.setMainApplication(this);
            primaryStage.hide();
            root.setCenter(pane);
            primaryStage.sizeToScene();
            primaryStage.centerOnScreen();
            if (currentGlobalStage == GlobalStage.UPDATE) {
                primaryStage.setResizable(false);
                primaryStage.setOnCloseRequest(Event::consume);
            }
            primaryStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
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
