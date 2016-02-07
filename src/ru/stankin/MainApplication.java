package ru.stankin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.stankin.test.holders.QuestionsHolder;
import ru.stankin.utils.ImageCache;

import java.net.URL;


/**
 * Created by DisDev on 21.01.2016.
 */
public class MainApplication extends Application {

    private Stage primaryStage;
    private GlobalStage currentGlobalStage;
    private BorderPane root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        QuestionsHolder.getInstance();
        ImageCache.getInstance();
        primaryStage.setTitle("Lab++");
        nextStage();
        primaryStage.show();
    }

    private static void main(String args[]) {
        launch(args);
    }

    private void prepareUI() {
        try {
            if (root == null) {
                root = FXMLLoader.load(getClass().getResource("mainFrame.fxml"));
                primaryStage.setScene(new Scene(root));
            }
            URL url = getClass().getResource(currentGlobalStage.getPathToForm());
            FXMLLoader loader = new FXMLLoader(url);
            Pane pane = loader.load();
            AbstractController controller = loader.getController();
            controller.setMainApplication(this);
            root.setPrefSize(pane.getPrefWidth(), pane.getPrefHeight());
            root.setCenter(pane);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void nextStage() {
        currentGlobalStage = currentGlobalStage == null ? GlobalStage.WELCOME : currentGlobalStage.next();
        prepareUI();
    }

    private enum GlobalStage {
        WELCOME("welcome.fxml"),
        TEST("test/frameForTest.fxml"),
        MAIN_LAB_WORK("work/workFrame.fxml");

        private final String pathToForm;

        GlobalStage(String pathToForm) {
            this.pathToForm = pathToForm;
        }

        public String getPathToForm() {
            return pathToForm;
        }

        public GlobalStage next() {
            return this == MAIN_LAB_WORK ? this : values()[ordinal() + 1];
        }
    }

}
