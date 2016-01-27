package ru.stankin.controllers;

import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.stankin.holders.VariableHolder;

/**
 * Created by DisDev on 27.01.2016.
 */
public class Scene3DController {

    private final Stage stage;
    private final VariableHolder holder;

    public Scene3DController(Stage mainStage, VariableHolder variableHolder) {
        stage = new Stage();
        stage.initModality(Modality.NONE);
        stage.initOwner(mainStage);
        stage.setTitle("3D модель");
        stage.setIconified(false);
        stage.setHeight(400);
        stage.setWidth(600);
        stage.setResizable(false);

        holder = variableHolder;

    }

    public void buildAndShow() {

    }
}
