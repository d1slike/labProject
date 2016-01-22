package ru.stankin;


import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import ru.stankin.holders.VariableHolder;
import ru.stankin.model.VariableType;

import java.util.Map;

/**
 * Created by Dislike on 22.01.2016.
 */
public class MainController {

    private final VariableHolder variableHolder;

    @FXML
    private ComboBox<VariableType> altVarSwitcher;

    public MainController() {
        variableHolder = new VariableHolder();
    }

    @FXML
    private void initialize() {
        ObservableList<VariableType> items = altVarSwitcher.getItems();
        items.addAll(VariableType.values());
        altVarSwitcher.setValue(items.get(0));
    }

    @FXML
    private void onChangedAltVariable() {
        variableHolder.setAltVariable(altVarSwitcher.getValue());
    }

}
