package ru.stankin.work.subcontrollers;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.stankin.work.managers.VariableManager;
import ru.stankin.work.model.ResultRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by DisDev on 27.01.2016.
 */
public class ChartController {

    private static final int CHART_HEIGHT = 400;
    private static final int CHART_WIDTH = 300;

    private final NumberAxis yAxisForDynamicReactionsChart;
    private final NumberAxis yAxixForFullReqctionsChart;
    private final LineChart<Number, Number> dynamicReactionsChart;
    private final LineChart<Number, Number> fullReactionsChart;
    private final Stage stage;

    private boolean alreadyBuilt;

    public ChartController(Stage primaryStage) {
        yAxisForDynamicReactionsChart = new NumberAxis();
        yAxixForFullReqctionsChart = new NumberAxis();

        NumberAxis xAxisForDynamic = new NumberAxis();
        NumberAxis xAsisForFull = new NumberAxis();

        final String xAxisName = "Время(t)";
        xAxisForDynamic.setLabel(xAxisName);
        xAsisForFull.setLabel(xAxisName);

        dynamicReactionsChart = new LineChart<>(xAxisForDynamic, yAxisForDynamicReactionsChart);
        fullReactionsChart = new LineChart<>(xAsisForFull, yAxixForFullReqctionsChart);
        dynamicReactionsChart.setPrefSize(CHART_WIDTH, CHART_HEIGHT);
        fullReactionsChart.setPrefSize(CHART_WIDTH, CHART_HEIGHT);

        BorderPane pane = new BorderPane();
        pane.setPrefSize(CHART_WIDTH * 2, CHART_HEIGHT);
        pane.setLeft(dynamicReactionsChart);
        pane.setRight(fullReactionsChart);

        Scene scene = new Scene(pane);
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);
        stage.setTitle("Графики");
        stage.setIconified(false);
        stage.setHeight(CHART_HEIGHT);
        stage.setWidth(CHART_WIDTH * 2);
        stage.setResizable(false);
        stage.setScene(scene);
    }

    public void buildAndShow(VariableManager variableManager) {

        if (!alreadyBuilt) {
            final String researchVarName = variableManager.getResearchVariable().getName();
            yAxixForFullReqctionsChart.setLabel(researchVarName + " полная");
            yAxisForDynamicReactionsChart.setLabel(researchVarName + " динамическая");
            dynamicReactionsChart.getData().addAll(buildLines(variableManager, false));
            fullReactionsChart.getData().addAll(buildLines(variableManager, true));
            alreadyBuilt = true;
        }
        stage.showAndWait();
    }

    private List<XYChart.Series<Number, Number>> buildLines(VariableManager variableManager, boolean fullReaction) {
        final int altVarStepsCount = VariableManager.ALT_VAR_MAX_STEP_COUNT;
        final ObservableList<ResultRecord> resultRecords = variableManager.getResultRecords();
        final String altVarName = variableManager.getAltVariable().getName();

        int currentPos = 0;

        XYChart.Series<Number, Number> firstLine = new XYChart.Series<>();
        firstLine.setName(altVarName + " = " + resultRecords.get(currentPos).getAltVar());
        for (; currentPos < altVarStepsCount; currentPos++) {
            ResultRecord record = resultRecords.get(currentPos);
            firstLine.getData().add(new XYChart.Data<>(record.getTime(), fullReaction ? record.getFullReaction() : record.getDynamicReaction()));
        }
        XYChart.Series<Number, Number> secondLine = new XYChart.Series<>();
        secondLine.setName(altVarName + " = " + resultRecords.get(currentPos).getAltVar());
        for (; currentPos < altVarStepsCount * 2; currentPos++) {
            ResultRecord record = resultRecords.get(currentPos);
            secondLine.getData().add(new XYChart.Data<>(record.getTime(), fullReaction ? record.getFullReaction() : record.getDynamicReaction()));
        }
        XYChart.Series<Number, Number> thirdLine = new XYChart.Series<>();
        thirdLine.setName(altVarName + " = " + resultRecords.get(currentPos).getAltVar());
        for (; currentPos < altVarStepsCount * 3; currentPos++) {
            ResultRecord record = resultRecords.get(currentPos);
            thirdLine.getData().add(new XYChart.Data<>(record.getTime(), fullReaction ? record.getFullReaction() : record.getDynamicReaction()));
        }
        XYChart.Series<Number, Number> fourthLine = new XYChart.Series<>();
        fourthLine.setName(altVarName + " = " + resultRecords.get(currentPos).getAltVar());
        for (; currentPos < altVarStepsCount * 4; currentPos++) {
            ResultRecord record = resultRecords.get(currentPos);
            fourthLine.getData().add(new XYChart.Data<>(record.getTime(), fullReaction ? record.getFullReaction() : record.getDynamicReaction()));
        }

        List<XYChart.Series<Number, Number>> lines = new ArrayList<>(4);
        Stream.of(firstLine, secondLine, thirdLine, fourthLine).forEach(lines::add);
        return lines;
    }

    public void clear() {
        if (alreadyBuilt) {
            fullReactionsChart.getData().clear();
            dynamicReactionsChart.getData().clear();
            alreadyBuilt = false;
        }
    }
}
