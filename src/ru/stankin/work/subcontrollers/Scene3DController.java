package ru.stankin.work.subcontrollers;

import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.stankin.work.managers.VariableManager;

/**
 * Created by DisDev on 27.01.2016.
 */
public class Scene3DController {

    private final Stage stage;

    public Scene3DController() {
        stage = new Stage();
        stage.initModality(Modality.NONE);
        stage.setTitle("3D модель");
        stage.setIconified(false);
        stage.setHeight(400);
        stage.setWidth(600);
        stage.setResizable(false);


    }

    public void buildAndShow(VariableManager variableManager, Stage primaryStage) {
    }
}
