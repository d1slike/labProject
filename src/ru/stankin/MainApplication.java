package ru.stankin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.stankin.work.WorkController;


/**
 * Created by DisDev on 21.01.2016.
 */
public class MainApplication extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Lab++");
        //primaryStage.setResizable(false);
        //primaryStage.setFullScreen(true);
        initMainForm();

    }

    private static void main(String args[]) {
        launch(args);
    }

    private void initMainForm() {
        try {
            FXMLLoader mainFrameLoader = new FXMLLoader();
            mainFrameLoader.setLocation(getClass().getResource("mainFrame.fxml"));
            BorderPane root = mainFrameLoader.load();

            FXMLLoader workFrameLoader = new FXMLLoader();
            workFrameLoader.setLocation(getClass().getResource("workFrame.fxml"));
            BorderPane workFrame = workFrameLoader.load();
            WorkController controller = workFrameLoader.getController();
            controller.setMainStage(primaryStage);

            root.setCenter(workFrame);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
