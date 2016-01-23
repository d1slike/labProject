package ru.stankin;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.stankin.holders.InterfaceItemHolder;
import ru.stankin.holders.VariableHolder;
import ru.stankin.math.Calculator;
import ru.stankin.model.Stage;
import ru.stankin.model.Variable;
import ru.stankin.model.VariableType;

/**
 * Created by Dislike on 22.01.2016.
 */
public class MainController {

    private VariableHolder variableHolder;
    private InterfaceItemHolder interfaceItemHolder;
    private Stage currentStage;

    @FXML
    private ComboBox<VariableType> altVarSwitcher;
    @FXML
    private TextField altVarStepField;

    @FXML
    private TableView<Variable> varTable;
    @FXML
    private TableColumn<Variable, String> varTableColumnParam;
    @FXML
    private TableColumn<Variable, Double> varTableColumnValue;

    @FXML
    private Button showIn3DButton;
    @FXML
    private Button resetButton;

    @FXML
    private ComboBox<VariableType> researchVarSwitcher;
    @FXML
    private TextField timeField;
    @FXML
    private Button calcButton;

    @FXML
    private TableView<Variable> resultTable;
    @FXML
    private TableColumn<Variable, Double> resultTableTimeColumn;
    @FXML
    private TableColumn<Variable, Double> resultTableAltVarColumn;
    @FXML
    private TableColumn<Variable, Double> resultTableDynamicReaction;
    @FXML
    private TableColumn<Variable, Double> resultTableFullReaction;

    @FXML
    private Button showChartButton;
    @FXML
    private Button cancelButton;

    @FXML
    private Label informationTextLabel;
    @FXML
    private Button nextStageButton;

    public MainController() {
    }

    @FXML
    private void initialize() {

        variableHolder = new VariableHolder();
        Calculator.initVarHolder(variableHolder);
        interfaceItemHolder = new InterfaceItemHolder(getClass());
        interfaceItemHolder.putItem("altVarSwitcher", altVarSwitcher);
        interfaceItemHolder.putItem("altVarStepField", altVarStepField);
        interfaceItemHolder.putItem("varTable", varTable);
        interfaceItemHolder.putItem("showIn3DButton", showIn3DButton);
        interfaceItemHolder.putItem("resetButton", resetButton);
        interfaceItemHolder.putItem("researchVarSwitcher", researchVarSwitcher);
        interfaceItemHolder.putItem("resultTable", resultTable);
        interfaceItemHolder.putItem("showChartButton", showChartButton);
        interfaceItemHolder.putItem("calcButton", calcButton);
        interfaceItemHolder.putItem("nextStageButton", nextStageButton);
        interfaceItemHolder.putItem("timeField", timeField);
        interfaceItemHolder.putItem("cancelButton", cancelButton);

        onNextStageButtonClick();

        ObservableList<VariableType> items = altVarSwitcher.getItems();
        items.addAll(VariableType.values());
        altVarSwitcher.setValue(items.get(0));

        varTableColumnParam.setCellValueFactory(param -> param.getValue().getType().getNameProperty());
        varTableColumnValue.setCellValueFactory(param -> param.getValue().getValuePropertie());
        varTable.getItems().addAll(variableHolder.getAllVars());
    }

    @FXML
    private void onChangedAltVariable() {
        variableHolder.setAltVariable(altVarSwitcher.getValue());
    }

    @FXML
    private void onNextStageButtonClick() {
        if (currentStage == null)
            currentStage = Stage.STAGE_1_SELECT_ALT_VAR;
        else
            currentStage = currentStage.nextStage();

        interfaceItemHolder.prepareInterfaceForCurrentStage(currentStage);
        informationTextLabel.setText(currentStage.getDescription());

        switch (currentStage) {
            case STAGE_1_SELECT_ALT_VAR:
                break;
            case STAGE_2_WRITE_STEP_TO_ALT_VAR:
                break;
        }

    }

}
