package ru.stankin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created by DisDev on 21.01.2016.
 */
public class MainApplication extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Lab++");
        primaryStage.setResizable(false);
        initMainLayout();

    }

    private static void main(String args[]) {
        launch(args);
    }

    private void initMainLayout() {
        try {
            FXMLLoader mainFrameLoader = new FXMLLoader();
            mainFrameLoader.setLocation(getClass().getResource("view/mainFrame.fxml"));
            BorderPane root = mainFrameLoader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
