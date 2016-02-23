package ru.stankin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.stankin.test.holders.QuestionsHolder;
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
        try {
            File file = new File("resources/image/icon.png");
            if (file.exists())
                MAIN_APP_ICON = new Image(file.toURI().toURL().toString());
            else
                throw new FileNotFoundException();
        } catch (Exception ex) {
            Util.showMessageAndCloseProgram("Повреждение файлов программы!", "Пожалуйста переустановите программу и попробуйте запустить повторно.");
        }
        QuestionsHolder.getInstance();
        ImageCache.getInstance();
        this.primaryStage = primaryStage;
        primaryStage.getIcons().add(MAIN_APP_ICON);
        primaryStage.setTitle(PROGRAM_NAME);
        nextStage();
        //nextStage();
        //nextStage();
        primaryStage.show();
    }

    public void nextStage() {
        currentGlobalStage = currentGlobalStage == null ? GlobalStage.WELCOME : currentGlobalStage.next();
        prepareUI();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private static void main(String args[]) {
        launch(args);
    }

    public static Image getIcon() {
        return MAIN_APP_ICON;
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

            URL url = getClass().getResource(currentGlobalStage.getPathToForm());
            FXMLLoader loader = new FXMLLoader(url);
            Pane pane = loader.load();
            activeController = loader.getController();
            activeController.setMainApplication(this);
            root.setCenter(pane);
            primaryStage.sizeToScene();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private enum GlobalStage {
        WELCOME("welcome.fxml"),
        TEST("test/frameForTest.fxml"),
        MAIN_LAB_WORK("work/workFrame.fxml");

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
