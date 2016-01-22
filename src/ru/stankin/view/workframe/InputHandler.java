package ru.stankin.view.workframe;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ru.stankin.model.Variables;



/**
 * Created by DisDev on 22.01.2016.
 */
public class InputHandler {

    private Variables vars;
    @FXML
    private TextField HTextField;

    private InputHandler() {
        vars = new Variables();
    }

    public double getValue(String name) {
        name = name.toLowerCase();
        switch (name) {
            case "h":
                return vars.getH();
            case "r":
                return vars.getR();
            case "l":
                return vars.getL();
            case "e":
                return vars.getE();
            case "zc":
                return vars.getZC();
            case "ro":
                return vars.getRO();
            case "gamma":
                return vars.getGAMMA();
            case "m":
                return vars.getM();
        }
        return -99999999;
    }

    @FXML
    private void handleCalculationCommand() {
        vars.setH(Double.parseDouble(HTextField.getText()));
    }

}
