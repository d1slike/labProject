package ru.stankin.work;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import ru.stankin.AbstractController;
import ru.stankin.work.managers.UIManager;
import ru.stankin.work.managers.VariableManager;
import ru.stankin.work.subcontrollers.ChartController;
import ru.stankin.work.subcontrollers.Scene3DController;
import ru.stankin.work.model.ResultRecord;
import ru.stankin.enums.WorkStage;
import ru.stankin.work.model.Variable;
import ru.stankin.enums.VariableType;
import ru.stankin.enums.ElementNames;

import java.util.regex.Pattern;


/**
 * Created by Dislike on 22.01.2016.
 */
public class WorkController extends AbstractController{

    private static final Pattern STRING_VALIDATOR = Pattern.compile("\\-?\\d+(\\.\\d{0,})?");

    private VariableManager variableManager;
    private UIManager UIManager;
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

    public WorkController() {
    }

    @FXML
    private void initialize() {

        variableManager = new VariableManager();
        chartController = new ChartController(mainStage);
        scene3DController = new Scene3DController(mainStage);
        UIManager = new UIManager();
        UIManager.putItem(ElementNames.COMBO_BOX_ALT_VAR_SWITCHER, altVarSwitcher);
        UIManager.putItem(ElementNames.FIELD_ALT_VAR_STEP, altVarStepField);
        UIManager.putItem(ElementNames.TABLE_VARIABLES, varTable);
        UIManager.putItem(ElementNames.BUTTON_SHOW_IN_3D, showIn3DButton);
        //UIManager.putItem(ElementNames.BUTTON_RESET_ALL_INPUT_VALUES, resetButton);
        UIManager.putItem(ElementNames.COMBO_BOX_RESEARCH_VAR_SWITCHER, researchVarSwitcher);
        UIManager.putItem(ElementNames.TABLE_RESULTS, resultTable);
        UIManager.putItem(ElementNames.BUTTON_SHOW_CHART, showChartButton);
        UIManager.putItem(ElementNames.BUTTON_CALCULATE, calcButton);
        UIManager.putItem(ElementNames.BUTTON_NEXT_STAGE, nextStageButton);
        UIManager.putItem(ElementNames.FIELD_TIME, timeField);
        UIManager.putItem(ElementNames.BUTTON_CANCEL, cancelButton);
        UIManager.putItem(ElementNames.BUTTON_PREV_STAGE, prevStageButton);

        //currentWorkStage = WorkStage.STAGE_1_SELECT_ALT_VAR;
        //currentWorkStage = WorkStage.STAGE_4_SELECT_RESEARCH_VAR;

        currentWorkStage = WorkStage.STAGE_6_CHECK_CHART;
        variableManager.setResearchVariableType(VariableType.Yb);
        variableManager.setAltVariable(VariableType.R);
        variableManager.setVal(VariableType.H, 1.0);
        variableManager.setVal(VariableType.R, 0.1);
        variableManager.setVal(VariableType.L, 0.7);
        variableManager.setVal(VariableType.E, 0.001);
        variableManager.setVal(VariableType.Zc, 0.3);
        variableManager.setVal(VariableType.RO, 8000);
        variableManager.setVal(VariableType.GAMMA, 4);
        variableManager.setVal(VariableType.M, 0.6);
        onChangedWorkStage();
        variableManager.calculateNextForTime(50.0);


        altVarSwitcher.getItems().addAll(VariableManager.EDITABLE_VAR_TYPES_ARRAY);
        altVarSwitcher.setValue(VariableType.RO);

        researchVarSwitcher.getItems().addAll(VariableType.Xa, VariableType.Xb, VariableType.Ya, VariableType.Yb);
        researchVarSwitcher.setValue(VariableType.Xa);

        varTableColumnParam.setCellValueFactory(param -> param.getValue().getType().getNameWithMeansurement());
        varTableColumnValue.setCellValueFactory(param -> param.getValue().getValueProperties());
        varTableColumnValue.setCellFactory((TableColumn<Variable, Number> col) -> new CellForVarTable());
        varTable.getItems().addAll(variableManager.getAllVars());

        resultTableTimeColumn.setCellValueFactory(param -> param.getValue().timeProperty());
        resultTableTimeColumn.setCellFactory((TableColumn<ResultRecord, Number> col) -> new CellForResultTable());
        resultTableAltVarColumn.setCellValueFactory(param -> param.getValue().altVarProperty());
        resultTableAltVarColumn.setCellFactory((TableColumn<ResultRecord, Number> col) -> new CellForResultTable());
        resultTableStaticReaction.setCellValueFactory(param -> param.getValue().staticReactionProperty());
        resultTableStaticReaction.setCellFactory((TableColumn<ResultRecord, Number> col) -> new CellForResultTable());
        resultTableDynamicReaction.setCellValueFactory(param -> param.getValue().dynamicReactionProperty());
        resultTableDynamicReaction.setCellFactory((TableColumn<ResultRecord, Number> col) -> new CellForResultTable());
        resultTableFullReaction.setCellValueFactory(param -> param.getValue().fullReactionProperty());
        resultTableFullReaction.setCellFactory((TableColumn<ResultRecord, Number> col) -> new CellForResultTable());
        resultTableRPMColumn.setCellValueFactory(param -> param.getValue().getRPM());
        resultTable.setItems(variableManager.getResultRecords());


        //final UnaryOperator<TextFormatter.Change> condition = change -> STRING_VALIDATOR.matcher(change.getControlNewText()).matches() ? change : null;
        //timeField.setTextFormatter(new TextFormatter<Number>(condition));
        //altVarStepField.setTextFormatter(new TextFormatter<Number>(condition));


    }

    @FXML
    private void onChangedAltVariable() {
        variableManager.setAltVariable(altVarSwitcher.getValue());
    }

    @FXML
    private void onChangedResearchVariable() {
        variableManager.setResearchVariableType(researchVarSwitcher.getValue());
    }

    @FXML
    private void on3DButtonShowClick() {
        /*Scene3DController controller = new Scene3DController(mainStage, variableManager);
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
            if(!variableManager.checkTime(time))
                throw new NumberFormatException();
            timeField.setStyle(UIManager.DEFAULT_BORDER_STYLE);
        } catch (Exception ex) {
            timeField.setStyle(UIManager.RED_BORDER_STYLE);
            return;
        }

        if(variableManager.calculateNextForTime(time))
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
        chartController.buildAndShow(variableManager);
    }

    private void onChangedWorkStage() {
        UIManager.prepareInterfaceForCurrentStage(currentWorkStage);
        informationTextLabel.setText(currentWorkStage.getDescription());
    }

    private boolean executeAllActionsOnCurrentStage() {
        switch (currentWorkStage) {
            case STAGE_1_SELECT_ALT_VAR:
                resultTableAltVarColumn.setText(variableManager.getAltVariable().getName());
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
                    variableManager.setAltVarStep(value);
                else {
                    UIManager.setRedBorder(ElementNames.FIELD_ALT_VAR_STEP);
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
                    UIManager.setRedBorder(ElementNames.TABLE_VARIABLES);
                    return false;
                }
            }
            break;
            case STAGE_4_SELECT_RESEARCH_VAR: {
                String name = variableManager.getResearchVariable().getName();
                resultTableStaticReaction.setText(name + "(стат.)");
                resultTableDynamicReaction.setText(name + "(динам.)");
                resultTableFullReaction.setText(name + "(полн.)");


                /*variableManager.getResultRecords().addAll(new ResultRecord(1, 2, 3, 4, 5), new ResultRecord(10, 1, 1, 3, 4), new ResultRecord(2, 2, 3, 4, 5), new ResultRecord(4, 2, 3, 4, 5),
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


    private class CellForVarTable extends TextFieldTableCell<Variable, Number> {
        public CellForVarTable() {
            setConverter(new StringDoubleConverter());
        }

        private class StringDoubleConverter extends StringConverter<Number> {

            @Override
            public String toString(Number object) {
                return String.format("%.3f", object.doubleValue());
            }

            @Override
            public Double fromString(String string) {
                double val;
                try {
                    val = Double.parseDouble(string);
                    VariableType type = varTable.getItems().get(getIndex()).getType();
                    if (!type.checkRange(val))
                        throw new NumberFormatException();
                    setStyle(UIManager.DEFAULT_BORDER_STYLE);
                } catch (Exception ex) {
                    setStyle(UIManager.RED_BORDER_STYLE);
                    return 0.;
                }
                return val;
            }
        }
    }

    private class CellForResultTable extends TextFieldTableCell<ResultRecord, Number> {
        public CellForResultTable() {
            setConverter(new StringDoubleConverter());
        }

        private class StringDoubleConverter extends StringConverter<Number> {

            @Override
            public String toString(Number object) {
                return String.format("%.3f", object.doubleValue());
            }

            @Override
            public Double fromString(String string) {
                return 0.;
            }
        }
    }
}
