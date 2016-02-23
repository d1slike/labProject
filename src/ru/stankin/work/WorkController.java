package ru.stankin.work;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import ru.stankin.AbstractController;
import ru.stankin.enums.ElementNames;
import ru.stankin.enums.VariableType;
import ru.stankin.enums.WorkStage;
import ru.stankin.utils.Util;
import ru.stankin.work.managers.UIManager;
import ru.stankin.work.managers.VariableManager;
import ru.stankin.work.model.ResultRecord;
import ru.stankin.work.model.Variable;
import ru.stankin.work.subcontrollers.ChartController;

import java.util.regex.Pattern;
import java.util.stream.Stream;


/**
 * Created by Dislike on 22.01.2016.
 */
public class WorkController extends AbstractController {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("(?:\\d*\\.)?\\d+");

    private VariableManager variableManager;
    private UIManager uiManager;
    private ChartController chartController;
    private WorkStage currentWorkStage;

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
    private TableColumn<ResultRecord, Number> resultTablePhiColumn;

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

    @FXML
    private Label timeLabel;

    @FXML
    private VBox currentInfoVBox;
    @FXML
    private Label altVarNameLabel;
    @FXML
    private Label altVarValueLabel;
    @FXML
    private Label RPMValueLabel;

    @FXML
    private Label phiNameLabel;
    @FXML
    private Label phiValueLabel;

    public WorkController() {
    }

    @Override
    public void prepareForNext() {

    }

    @FXML
    private void initialize() {

        variableManager = new VariableManager();
        chartController = new ChartController();
        currentWorkStage = WorkStage.STAGE_1_SELECT_ALT_VAR;

        altVarSwitcher.getItems().addAll(VariableManager.EDITABLE_VAR_TYPES_ARRAY);
        altVarSwitcher.getItems().remove(VariableType.TAU);
        altVarSwitcher.setValue(VariableType.RO);

        researchVarSwitcher.getItems().addAll(VariableType.Xa, VariableType.Xb, VariableType.Ya, VariableType.Yb);
        researchVarSwitcher.setValue(VariableType.Xa);

        timeLabel.setText(VariableType.VarName.DELTA + VariableType.T.getName());

        resultTable.setVisible(false);

        prepareVarTable();
        prepareResultTable();
        prepareUI();

        currentInfoVBox.setVisible(false);
        resultTablePhiColumn.setText(VariableType.VarName.PHI);

        variableManager.setVal(VariableType.H, 1.0);
        variableManager.setVal(VariableType.R, 0.1);
        variableManager.setVal(VariableType.L, 0.7);
        variableManager.setVal(VariableType.E, 0.001);
        //variableManager.setVal(VariableType.Zc, 0.3);
        variableManager.setVal(VariableType.RO, 8000);
        variableManager.setVal(VariableType.GAMMA, 4);
        variableManager.setVal(VariableType.M, 0.6);
        variableManager.setVal(VariableType.TAU, 50);
        variableManager.setAltVarStep(0.02);
        variableManager.setCurrentDeltaTime(0.01911);

        //variableManager.setResearchVariableType(VariableType.Xb);
        //variableManager.setAltVariable(VariableType.R);


        //calculateAndShowInformation();
        //variableManager.calculate();

        EventHandler<KeyEvent> keyEventEventHandler = event -> {
            if (event.getCode() == KeyCode.ENTER)
                onNextStageButtonClick();
        };
        Stream.of(varTable, altVarStepField, nextStageButton, altVarSwitcher, researchVarSwitcher)
                .forEach(control -> control.setOnKeyPressed(keyEventEventHandler));
        timeField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                onCalcButtonClick();
        });
        //final UnaryOperator<TextFormatter.Change> condition = change -> NUMBER_PATTERN.matcher(change.getControlNewText()).matches() ? change : null;
        //timeField.setTextFormatter(new TextFormatter<Number>(condition));
        //altVarStepField.setTextFormatter(new TextFormatter<Number>(condition));


    }

    private void prepareUI() {
        uiManager = new UIManager();
        uiManager.putItem(ElementNames.COMBO_BOX_ALT_VAR_SWITCHER, altVarSwitcher);
        uiManager.putItem(ElementNames.FIELD_ALT_VAR_STEP, altVarStepField);
        uiManager.putItem(ElementNames.TABLE_VARIABLES, varTable);
        uiManager.putItem(ElementNames.COMBO_BOX_RESEARCH_VAR_SWITCHER, researchVarSwitcher);
        uiManager.putItem(ElementNames.TABLE_RESULTS, resultTable);
        uiManager.putItem(ElementNames.BUTTON_SHOW_CHART, showChartButton);
        uiManager.putItem(ElementNames.BUTTON_CALCULATE, calcButton);
        uiManager.putItem(ElementNames.BUTTON_NEXT_STAGE, nextStageButton);
        uiManager.putItem(ElementNames.FIELD_TIME, timeField);
        uiManager.putItem(ElementNames.BUTTON_CANCEL, cancelButton);
        uiManager.putItem(ElementNames.BUTTON_PREV_STAGE, prevStageButton);
        onChangedWorkStage();
    }


    private void prepareResultTable() {
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
        resultTablePhiColumn.setCellFactory((TableColumn<ResultRecord, Number> col) -> new CellForResultTable());
        resultTablePhiColumn.setCellValueFactory(param -> param.getValue().phiInDegreesProperty());
        resultTable.setItems(variableManager.getResultRecords());

        resultTable.setRowFactory(param1 -> {
            TableRow<ResultRecord> tableRow = new TableRow<>();
            tableRow.itemProperty().addListener(((observable, oldValue, newValue) -> {

                if (newValue == null)
                    return;
                int index = newValue.getIndex();
                String color = "";
                if (index >= 0 && index < 11)
                    color = ChartController.FIRST_LINE_COLOR;
                else if (index >= 11 && index < 22)
                    color = ChartController.SECOND_LINE_COLOR;
                else if (index >= 22 && index < 33)
                    color = ChartController.THIRD_LINE_COLOR;
                else if (index >= 33 && index < 44)
                    color = ChartController.FORTH_LINE_COLOR;
                tableRow.setStyle("-fx-background-color: #" + color);
            }));
            return tableRow;
        });
    }

    private void prepareVarTable() {
        varTableColumnParam.setCellValueFactory(param -> param.getValue().getType().getNameWithMeansurement());
        varTableColumnParam.setStyle("-fx-font-weight: bold;");
        varTableColumnValue.setCellValueFactory(param -> param.getValue().getValueProperties());
        varTableColumnValue.setCellFactory((TableColumn<Variable, Number> col) -> new CellForVarTable());
        varTable.getItems().addAll(variableManager.getAllVars());
    }

    @FXML
    private void onCancelButtonClick() {
        if (chartController.isInShowing())
            return;
        currentWorkStage = WorkStage.STAGE_1_SELECT_ALT_VAR;
        onChangedWorkStage();
        resultTable.setVisible(false);
        currentInfoVBox.setVisible(false);
        variableManager.clear();
        chartController.clear();
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
    private void onCalcButtonClick() {
        if (currentWorkStage != WorkStage.STAGE_5_WRITE_TIME_STEP)
            return;
        ParseResult result = getDoubleValue(timeField, ElementNames.FIELD_TIME);
        if (!result.success)
            return;
        variableManager.setCurrentDeltaTime(result.value);
        variableManager.calculateAllReactions();
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
        if (currentWorkStage == WorkStage.STAGE_5_WRITE_TIME_STEP) {
            resultTable.setVisible(false);
            currentInfoVBox.setVisible(false);
        }
        currentWorkStage = currentWorkStage.prevStage();
        onChangedWorkStage();
    }

    @FXML
    private void onShowChartButtonClick() {
        if (chartController == null)
            return;
        chartController.buildAndShow(variableManager, getMainApplication().getPrimaryStage());
    }

    private void onChangedWorkStage() {
        uiManager.prepareInterfaceForCurrentStage(currentWorkStage);
        informationTextLabel.setText(currentWorkStage.getDescription());
    }

    private boolean executeAllActionsOnCurrentStage() {
        switch (currentWorkStage) {
            case STAGE_1_SELECT_ALT_VAR:
                resultTableAltVarColumn.setText(variableManager.getAltVariable().getName());
                altVarNameLabel.setText(variableManager.getAltVariable().getType().getNameWithMeansurement().getValue());
                break;
            case STAGE_2_WRITE_STEP_TO_ALT_VAR: {
                ParseResult result = getDoubleValue(altVarStepField, ElementNames.FIELD_ALT_VAR_STEP);
                if (result.success)
                    variableManager.setAltVarStep(result.value);
                return result.success;
            }
            case STAGE_3_FILL_VAR_TABLE: {
                boolean ok = true;
                for (Variable variable : varTable.getItems()) {
                    double val = variable.getValue();
                    VariableType type = variable.getType();
                    if (!(ok = type.checkRange(val)))
                        break;
                }
                if (!ok) {
                    uiManager.setRedBorder(ElementNames.TABLE_VARIABLES);
                    return false;
                }
            }
            break;
            case STAGE_4_SELECT_RESEARCH_VAR: {
                String name = variableManager.getResearchVariable().getName();
                resultTableStaticReaction.setText(name + "(стат.)");
                resultTableDynamicReaction.setText(name + "(динам.)");
                resultTableFullReaction.setText(name + "(полн.)");
                resultTable.setVisible(true);
                calculateAndShowInformation();
            }
            break;
            case STAGE_5_WRITE_TIME_STEP: {

                //todo check for complete
            }
        }

        return true;
    }

    private void calculateAndShowInformation() {
        altVarNameLabel.setText(variableManager.getAltVariable().getType().getNameWithMeansurement().getValue());
        altVarValueLabel.setText(Util.doubleFormat(variableManager.getAltVariable().getValue()) + "");
        phiNameLabel.setText(VariableType.VarName.PHI + "(" + VariableType.VarName.TAU + ")");
        phiValueLabel.setText(Util.doubleFormat(variableManager.calculatePhiForTau()));
        RPMValueLabel.setText(variableManager.calculateRPMForTau() + "");
        uiManager.playAnimationFor(ElementNames.FIELD_TIME);
        currentInfoVBox.setVisible(true);

    }

    private ParseResult getDoubleValue(TextField field, String elementName) {
        boolean success = true;
        double val = 0.;
        try {
            val = Double.parseDouble(field.getText());
            uiManager.setDefaultBorder(elementName);
        } catch (Exception ex) {
            uiManager.setRedBorder(elementName);
            success = false;
        }

        return new ParseResult(success, val);
    }


    private static class ParseResult {

        private final boolean success;
        private final double value;

        public ParseResult(boolean status, double value) {
            this.success = status;
            this.value = value;
        }
    }

    private static class CellForResultTable extends TextFieldTableCell<ResultRecord, Number> {
        public CellForResultTable() {
            setConverter(new StringDoubleConverter());
        }

        private class StringDoubleConverter extends StringConverter<Number> {

            @Override
            public String toString(Number object) {
                return Util.doubleFormat(object.doubleValue());
            }

            @Override
            public Double fromString(String string) {
                return 0.;
            }
        }
    }

    private class CellForVarTable extends TextFieldTableCell<Variable, Number> {
        public CellForVarTable() {
            setConverter(new StringDoubleConverter());
        }

        private class StringDoubleConverter extends StringConverter<Number> {

            @Override
            public String toString(Number object) {
                return Util.doubleFormat(object.doubleValue());
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
}
