package ru.stankin;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import ru.stankin.holders.InterfaceItemHolder;
import ru.stankin.holders.VariableHolder;
import ru.stankin.math.Calculator;
import ru.stankin.model.ResultRecord;
import ru.stankin.model.Stage;
import ru.stankin.model.Variable;
import ru.stankin.model.VariableType;
import ru.stankin.view.ChartHolder;
import ru.stankin.view.ElementNames;


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
    private TableView<ResultRecord> resultTable;
    @FXML
    private TableColumn<ResultRecord, Double> resultTableTimeColumn;
    @FXML
    private TableColumn<ResultRecord, Double> resultTableAltVarColumn;
    @FXML
    private TableColumn<ResultRecord, Double> resultTableStaticReaction;
    @FXML
    private TableColumn<ResultRecord, Double> resultTableDynamicReaction;
    @FXML
    private TableColumn<ResultRecord, Double> resultTableFullReaction;

    @FXML
    private Button showChartButton;
    @FXML
    private Button cancelButton;

    @FXML
    private Label informationTextLabel;
    @FXML
    private Button nextStageButton;
    @FXML
    private Button prevStageButton;

    public MainController() {
    }

    @FXML
    private void initialize() {

        variableHolder = new VariableHolder();
        Calculator.initVarHolder(variableHolder);
        interfaceItemHolder = new InterfaceItemHolder();
        interfaceItemHolder.putItem(ElementNames.COMBO_BOX_ALT_VAR_SWITCHER, altVarSwitcher);
        interfaceItemHolder.putItem(ElementNames.FIELD_ALT_VAR_STEP, altVarStepField);
        interfaceItemHolder.putItem(ElementNames.TABLE_VARIABLES, varTable);
        interfaceItemHolder.putItem(ElementNames.BUTTON_SHOW_IN_3D, showIn3DButton);
        interfaceItemHolder.putItem(ElementNames.BUTTON_RESET_ALL_INPUT_VALUES, resetButton);
        interfaceItemHolder.putItem(ElementNames.COMBO_BOX_RESEARCH_VAR_SWITCHER, researchVarSwitcher);
        interfaceItemHolder.putItem(ElementNames.TABLE_RESULTS, resultTable);
        interfaceItemHolder.putItem(ElementNames.BUTTON_SHOW_CHART, showChartButton);
        interfaceItemHolder.putItem(ElementNames.BUTTON_CALCULATE, calcButton);
        interfaceItemHolder.putItem(ElementNames.BUTTON_NEXT_STAGE, nextStageButton);
        interfaceItemHolder.putItem(ElementNames.FIELD_TIME, timeField);
        interfaceItemHolder.putItem(ElementNames.BUTTON_CANCEL, cancelButton);
        interfaceItemHolder.putItem(ElementNames.BUTTON_PREV_STAGE, prevStageButton);

        //currentStage = Stage.STAGE_1_SELECT_ALT_VAR;
        currentStage = Stage.STAGE_4_SELECT_RESEARCH_VAR;
        //currentStage = Stage.STAGE_6_CHECK_CHART;
        onChangedStage();


        altVarSwitcher.getItems().addAll(VariableHolder.EDITABLE_VAR_TYPES_ARRAY);
        altVarSwitcher.setValue(VariableType.RO);
        variableHolder.setAltVariable(VariableType.RO);

        researchVarSwitcher.getItems().addAll(VariableType.Xa, VariableType.Xb, VariableType.Ya, VariableType.Yb);
        researchVarSwitcher.setValue(VariableType.Xa);
        variableHolder.setResearchVariableType(VariableType.Xa);

        varTableColumnParam.setCellValueFactory(param -> param.getValue().getType().getNameProperty());
        varTableColumnValue.setCellValueFactory(param -> param.getValue().getValuePropertie());
        varTableColumnValue.setCellFactory((TableColumn<Variable, Double> col) -> new EditableCell());
        varTable.getItems().addAll(variableHolder.getAllVars());

    }

    @FXML
    private void onChangedAltVariable() {
        variableHolder.setAltVariable(altVarSwitcher.getValue());
    }

    @FXML
    private void onNextStageButtonClick() {
        if (currentStage == null)
            return;
        if (executeAllActionsOnCurrentStage()) {
            currentStage = currentStage.nextStage();
            onChangedStage();
        }
    }

    @FXML
    private void onPrevStageButtonClick() {
        if (currentStage == null || currentStage == Stage.STAGE_1_SELECT_ALT_VAR)
            return;
        currentStage = currentStage.prevStage();
        onChangedStage();
    }

    @FXML
    private void onShowChartButtonClick() {
        if (ChartHolder.getInstance() == null)
            return;
        ChartHolder.getInstance().show();
    }

    private void onChangedStage() {
        interfaceItemHolder.prepareInterfaceForCurrentStage(currentStage);
        informationTextLabel.setText(currentStage.getDescription());
    }

    private boolean executeAllActionsOnCurrentStage() {
        switch (currentStage) {
            case STAGE_1_SELECT_ALT_VAR:
                resultTableAltVarColumn.setText(variableHolder.getAltVariable().getName());
                break;
            case STAGE_2_WRITE_STEP_TO_ALT_VAR: {
                String stringValue = altVarStepField.getText();
                boolean ok = true;
                int value = -1;
                try {
                    value = Integer.parseInt(stringValue);
                    if (value < -130 || value > 60)
                        throw new NumberFormatException();
                } catch (Exception ex) {
                    ok = false;
                }
                if (ok)
                    variableHolder.getAltVariable().setStep(value);
                else {
                    interfaceItemHolder.setRedBorder(ElementNames.FIELD_ALT_VAR_STEP);
                    return false;
                }
            }
            break;
            case STAGE_3_FILL_VAR_TABLE: {
                boolean ok = true;
                for (Variable variable : varTable.getItems()) {
                    double val = variable.getValue();
                    VariableType type = variable.getType();
                    if (!(ok = type.checkRange(val)))
                        break;
                }
                if (!ok) {
                    interfaceItemHolder.setRedBorder(ElementNames.TABLE_VARIABLES);
                    return false;
                }
            }
            break;
            case STAGE_4_SELECT_RESEARCH_VAR: {
                String name = variableHolder.getResearchVariable().getName();
                resultTableStaticReaction.setText(name + "(стат.)");
                resultTableDynamicReaction.setText(name + "(динам.)");
                resultTableFullReaction.setText(name + "(полн.)");

                resultTableTimeColumn.setCellValueFactory(param -> param.getValue().timeProperty());
                resultTableAltVarColumn.setCellValueFactory(param -> param.getValue().altVarProperty());
                resultTableStaticReaction.setCellValueFactory(param -> param.getValue().staticReactionProperty());
                resultTableDynamicReaction.setCellValueFactory(param -> param.getValue().dynamicReactionProperty());
                resultTableFullReaction.setCellValueFactory(param -> param.getValue().fullReactionProperty());
                resultTable.setItems(variableHolder.getResultRecords());

                /*variableHolder.getResultRecords().addAll(new ResultRecord(1,2,3,4,5), new ResultRecord(10,1,1,3,4),  new ResultRecord(2,2,3,4,5), new ResultRecord(4,2,3,4,5),
                        new ResultRecord(1,2,3,4,5), new ResultRecord(10,1,1,3,4),  new ResultRecord(2,2,3,4,5), new ResultRecord(4,2,3,4,5),
                        new ResultRecord(1,2,3,4,5), new ResultRecord(10,1,1,3,4),  new ResultRecord(2,2,3,4,5), new ResultRecord(4,2,3,4,5),
                        new ResultRecord(1,2,3,4,88), new ResultRecord(10,1,1,3, 11),  new ResultRecord(2,2,3,4,3), new ResultRecord(4,2,3,4,1));
                currentStage = Stage.STAGE_5_FILL_RESULT_TABLE;
                ChartHolder.initChart(variableHolder.getResultRecords(), variableHolder.getResearchVariable().getName(), 4);*/
            }
            break;
            case STAGE_5_FILL_RESULT_TABLE: {
                ChartHolder.initChart(variableHolder.getResultRecords()
                        , variableHolder.getResearchVariable().getName(), 4); //todo useless magic const
            }
        }

        return true;
    }

    private class EditableCell extends TextFieldTableCell<Variable, Double> {
        public EditableCell() {
            setConverter(new StringDoubleConverter());
        }

        private class StringDoubleConverter extends StringConverter<Double> {

            private static final double NULL_CONST = 0.0;

            @Override
            public String toString(Double object) {
                return String.valueOf(object);
            }

            @Override
            public Double fromString(String string) {
                double val;
                try {
                    val = Double.parseDouble(string);
                    VariableType type = varTable.getItems().get(getIndex()).getType();
                    if (!type.checkRange(val))
                        throw new NumberFormatException();
                    setStyle(InterfaceItemHolder.DEFAULT_BORDER_STYLE);
                } catch (Exception ex) {
                    setStyle(InterfaceItemHolder.RED_BORDER_STYLE);
                    return NULL_CONST;
                }
                return val;
            }
        }
    }


}
