package ru.stankin;


import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import ru.stankin.controllers.ChartController;
import ru.stankin.controllers.Scene3DController;
import ru.stankin.holders.InterfaceItemHolder;
import ru.stankin.holders.VariableHolder;
import ru.stankin.model.ResultRecord;
import ru.stankin.enums.WorkStage;
import ru.stankin.model.Variable;
import ru.stankin.enums.VariableType;
import ru.stankin.enums.ElementNames;

import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Dislike on 22.01.2016.
 */
public class MainController {

    private static final Pattern STRING_VALIDATOR = Pattern.compile("\\-?\\d+(\\.\\d{0,})?");

    private VariableHolder variableHolder;
    private InterfaceItemHolder interfaceItemHolder;
    private ChartController chartController;
    private WorkStage currentWorkStage;
    private Scene3DController scene3DController;

    private Stage mainStage;

    @FXML
    private ComboBox<VariableType> altVarSwitcher;
    @FXML
    private TextField altVarStepField;

    @FXML
    private TableView<Variable> varTable;
    @FXML
    private TableColumn<Variable, String> varTableColumnParam;
    @FXML
    private TableColumn<Variable, Number> varTableColumnValue;

    @FXML
    private Button showIn3DButton;
    /*@FXML
    private Button resetButton;*/

    @FXML
    private ComboBox<VariableType> researchVarSwitcher;
    @FXML
    private TextField timeField;
    @FXML
    private Button calcButton;

    @FXML
    private TableView<ResultRecord> resultTable;
    @FXML
    private TableColumn<ResultRecord, Number> resultTableTimeColumn;
    @FXML
    private TableColumn<ResultRecord, Number> resultTableAltVarColumn;
    @FXML
    private TableColumn<ResultRecord, Number> resultTableStaticReaction;
    @FXML
    private TableColumn<ResultRecord, Number> resultTableDynamicReaction;
    @FXML
    private TableColumn<ResultRecord, Number> resultTableFullReaction;
    @FXML
    private TableColumn<ResultRecord, Number> resultTableRPMColumn;

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
        chartController = new ChartController(mainStage);
        scene3DController = new Scene3DController(mainStage, variableHolder);
        interfaceItemHolder = new InterfaceItemHolder();
        interfaceItemHolder.putItem(ElementNames.COMBO_BOX_ALT_VAR_SWITCHER, altVarSwitcher);
        interfaceItemHolder.putItem(ElementNames.FIELD_ALT_VAR_STEP, altVarStepField);
        interfaceItemHolder.putItem(ElementNames.TABLE_VARIABLES, varTable);
        interfaceItemHolder.putItem(ElementNames.BUTTON_SHOW_IN_3D, showIn3DButton);
        //interfaceItemHolder.putItem(ElementNames.BUTTON_RESET_ALL_INPUT_VALUES, resetButton);
        interfaceItemHolder.putItem(ElementNames.COMBO_BOX_RESEARCH_VAR_SWITCHER, researchVarSwitcher);
        interfaceItemHolder.putItem(ElementNames.TABLE_RESULTS, resultTable);
        interfaceItemHolder.putItem(ElementNames.BUTTON_SHOW_CHART, showChartButton);
        interfaceItemHolder.putItem(ElementNames.BUTTON_CALCULATE, calcButton);
        interfaceItemHolder.putItem(ElementNames.BUTTON_NEXT_STAGE, nextStageButton);
        interfaceItemHolder.putItem(ElementNames.FIELD_TIME, timeField);
        interfaceItemHolder.putItem(ElementNames.BUTTON_CANCEL, cancelButton);
        interfaceItemHolder.putItem(ElementNames.BUTTON_PREV_STAGE, prevStageButton);

        //currentWorkStage = WorkStage.STAGE_1_SELECT_ALT_VAR;
        currentWorkStage = WorkStage.STAGE_4_SELECT_RESEARCH_VAR;
        //currentWorkStage = WorkStage.STAGE_6_CHECK_CHART;
        onChangedWorkStage();


        altVarSwitcher.getItems().addAll(VariableHolder.EDITABLE_VAR_TYPES_ARRAY);
        altVarSwitcher.setValue(VariableType.RO);
        variableHolder.setAltVariable(VariableType.RO);

        researchVarSwitcher.getItems().addAll(VariableType.Xa, VariableType.Xb, VariableType.Ya, VariableType.Yb);
        researchVarSwitcher.setValue(VariableType.Xa);
        variableHolder.setResearchVariableType(VariableType.Xa);

        varTableColumnParam.setCellValueFactory(param -> param.getValue().getType().getNameProperty());
        varTableColumnValue.setCellValueFactory(param -> param.getValue().getValuePropertie());
        varTableColumnValue.setCellFactory((TableColumn<Variable, Number> col) -> new EditableCell());
        varTable.getItems().addAll(variableHolder.getAllVars());

        resultTableTimeColumn.setCellValueFactory(param -> param.getValue().timeProperty());
        resultTableAltVarColumn.setCellValueFactory(param -> param.getValue().altVarProperty());
        resultTableStaticReaction.setCellValueFactory(param -> param.getValue().staticReactionProperty());
        resultTableDynamicReaction.setCellValueFactory(param -> param.getValue().dynamicReactionProperty());
        resultTableFullReaction.setCellValueFactory(param -> param.getValue().fullReactionProperty());
        resultTableRPMColumn.setCellValueFactory(param -> param.getValue().getRPM());
        resultTable.setItems(variableHolder.getResultRecords());


        final UnaryOperator<TextFormatter.Change> condition = change -> STRING_VALIDATOR.matcher(change.getControlNewText()).matches() ? change : null;
        timeField.setTextFormatter(new TextFormatter<Number>(condition));
        altVarStepField.setTextFormatter(new TextFormatter<Number>(condition));


    }

    @FXML
    private void onChangedAltVariable() {
        variableHolder.setAltVariable(altVarSwitcher.getValue());
    }

    @FXML
    private void onChangedResearchVariable() {
        variableHolder.setResearchVariableType(researchVarSwitcher.getValue());
    }

    @FXML
    private void on3DButtonShowClick() {
        /*Scene3DController controller = new Scene3DController(mainStage, variableHolder);
        controller.buildAndShow();*/
    }

    @FXML
    private void onCalcButtonClick() {
        if (currentWorkStage != WorkStage.STAGE_5_FILL_RESULT_TABLE)
            return;
        double time;
        try {
            String timeInText = timeField.getText();
            time = Double.parseDouble(timeInText);
            if(!variableHolder.checkTime(time))
                throw new NumberFormatException();
            timeField.setStyle(InterfaceItemHolder.DEFAULT_BORDER_STYLE);
        } catch (Exception ex) {
            timeField.setStyle(InterfaceItemHolder.RED_BORDER_STYLE);
            return;
        }

        if(variableHolder.calculateNextForTime(time))
            onNextStageButtonClick();

    }

    @FXML
    private void onNextStageButtonClick() {
        if (currentWorkStage == null)
            return;
        if (executeAllActionsOnCurrentStage()) {
            currentWorkStage = currentWorkStage.nextStage();
            onChangedWorkStage();
        }
    }

    @FXML
    private void onPrevStageButtonClick() {
        if (currentWorkStage == null || currentWorkStage == WorkStage.STAGE_1_SELECT_ALT_VAR)
            return;
        currentWorkStage = currentWorkStage.prevStage();
        onChangedWorkStage();
    }

    @FXML
    private void onShowChartButtonClick() {
        if (chartController == null)
            return;
        chartController.buildAndShow(variableHolder);
    }

    private void onChangedWorkStage() {
        interfaceItemHolder.prepareInterfaceForCurrentStage(currentWorkStage);
        informationTextLabel.setText(currentWorkStage.getDescription());
    }

    private boolean executeAllActionsOnCurrentStage() {
        switch (currentWorkStage) {
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


                /*variableHolder.getResultRecords().addAll(new ResultRecord(1, 2, 3, 4, 5), new ResultRecord(10, 1, 1, 3, 4), new ResultRecord(2, 2, 3, 4, 5), new ResultRecord(4, 2, 3, 4, 5),
                        new ResultRecord(1, 2, 3, 4, 5, 1), new ResultRecord(10, 1, 1, 3, 4), new ResultRecord(2, 2, 3, 4, 5), new ResultRecord(4, 2, 3, 4, 5),
                        new ResultRecord(1, 2, 3, 4, 5), new ResultRecord(10, 1, 1, 3, 4), new ResultRecord(2, 2, 3, 4, 5), new ResultRecord(4, 2, 3, 4, 5),
                        new ResultRecord(1, 2, 3, 4, 88), new ResultRecord(10, 1, 1, 3, 11), new ResultRecord(2, 2, 3, 4, 3), new ResultRecord(4, 2, 3, 4, 1));
                currentWorkStage = WorkStage.STAGE_5_FILL_RESULT_TABLE;*/

            }
            break;
            case STAGE_5_FILL_RESULT_TABLE: {

                //todo check for complete
            }
        }

        return true;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }


    private class EditableCell extends TextFieldTableCell<Variable, Number> {
        public EditableCell() {
            setConverter(new StringDoubleConverter());

        }

        private class StringDoubleConverter extends StringConverter<Number> {

            private static final double NULL_CONST = 0.0;

            @Override
            public String toString(Number object) {
                return String.valueOf(object.doubleValue());
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
