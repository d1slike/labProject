package ru.stankin.work.subcontrollers;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.stankin.MainApplication;
import ru.stankin.utils.Util;
import ru.stankin.work.managers.VariableManager;
import ru.stankin.work.model.ResultRecord;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DisDev on 27.01.2016.
 */
public class ChartController {

    public static final String FIRST_LINE_COLOR = "ff7f50";
    public static final String SECOND_LINE_COLOR = "ffd700";
    public static final String THIRD_LINE_COLOR = "00a550";
    public static final String FORTH_LINE_COLOR = "75c1ff";
    private static final int CHART_HEIGHT = 600;
    private static final int CHART_WIDTH = 650;
    private final NumberAxis yAxisForDynamicReactionsChart;
    private final NumberAxis yAxisForFullReactionsChart;
    private final LineChart<Number, Number> dynamicReactionsChart;
    private final LineChart<Number, Number> fullReactionsChart;
    private final Stage stage;

    private boolean alreadyBuilt;
    private boolean inShowing;

    public ChartController(Stage primaryStage) {
        yAxisForDynamicReactionsChart = new NumberAxis();
        yAxisForFullReactionsChart = new NumberAxis();

        NumberAxis xAxisForDynamic = new NumberAxis();
        NumberAxis xAsisForFull = new NumberAxis();

        final String xAxisName = "Время t";
        xAxisForDynamic.setLabel(xAxisName);
        xAsisForFull.setLabel(xAxisName);

        dynamicReactionsChart = new LineChart<>(xAxisForDynamic, yAxisForDynamicReactionsChart);
        fullReactionsChart = new LineChart<>(xAsisForFull, yAxisForFullReactionsChart);
        dynamicReactionsChart.setPrefSize(CHART_WIDTH, CHART_HEIGHT);
        fullReactionsChart.setPrefSize(CHART_WIDTH, CHART_HEIGHT);

        dynamicReactionsChart.setCreateSymbols(true);
        dynamicReactionsChart.setTitle("Зависимость динамических реакций от времени за один оборот вала");
        fullReactionsChart.setCreateSymbols(true);
        fullReactionsChart.setTitle("Зависимость полных реакций от времени за один оборот вала");

        String url = "";
        try {
            url = new File("resources/css/Chart.css").toURI().toURL().toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        fullReactionsChart.getStylesheets().add(url);
        dynamicReactionsChart.getStylesheets().add(url);
        //fullReactionsChart.setStyle(cssStyleForChart);
        //dynamicReactionsChart.setStyle(cssStyleForChart);
        BorderPane pane = new BorderPane();
        //pane.setPrefSize(CHART_WIDTH * 2, CHART_HEIGHT);
        pane.setLeft(dynamicReactionsChart);
        pane.setRight(fullReactionsChart);


        Scene scene = new Scene(pane);
        stage = new Stage();
        stage.setTitle("Графики");
        //stage.initModality(Modality.NONE);
        //stage.setHeight(CHART_HEIGHT);
        //stage.setWidth(CHART_WIDTH * 2);
        stage.setIconified(false);
        stage.initOwner(primaryStage);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> inShowing = false);
    }

    public void buildAndShow(VariableManager variableManager) {

        if (inShowing)
            return;

        if (!alreadyBuilt) {
            stage.getIcons().clear();
            stage.getIcons().add(MainApplication.getIcon());
            final String researchVarName = variableManager.getResearchVariable().getName();
            yAxisForDynamicReactionsChart.setLabel(researchVarName + " динамическая");
            yAxisForFullReactionsChart.setLabel(researchVarName + " полная");

            List<ResultRecord> list = variableManager.getResultRecords();
            String altVarName = variableManager.getAltVariable().getName();
            dynamicReactionsChart.getData().addAll(buildLines(list, altVarName, false));
            fullReactionsChart.getData().addAll(buildLines(list, altVarName, true));
            alreadyBuilt = true;
        }
        inShowing = true;
        stage.show();
    }

    public void clear() {
        if (alreadyBuilt) {
            fullReactionsChart.getData().clear();
            dynamicReactionsChart.getData().clear();
            alreadyBuilt = false;
        }
    }

    public boolean isInShowing() {
        return inShowing;
    }

    private List<XYChart.Series<Number, Number>> buildLines(List<ResultRecord> resultRecords, String altVarName, boolean fullReaction) {
        final int maxTimeSteps = VariableManager.TIME_STEPS_COUNT;

        List<XYChart.Series<Number, Number>> list = new ArrayList<>();
        int currentPos = 0;
        int lastTime = maxTimeSteps;
        for (int i = 0; i < VariableManager.ALT_VAR_MAX_STEP_COUNT; i++, currentPos = lastTime, lastTime += maxTimeSteps)
            list.add(buildLine(resultRecords, currentPos, lastTime, altVarName, fullReaction));
        return list;
    }

    private XYChart.Series<Number, Number> buildLine(List<ResultRecord> data, int firstIndex, int lastIndex, String altVarName, boolean fullReaction) {
        XYChart.Series<Number, Number> line = new XYChart.Series<>();
        line.setName(altVarName + " = " + Util.doubleCommaFormat(data.get(firstIndex).getAltVar()) + " ");
        for (int i = firstIndex; i < lastIndex; i++) {
            ResultRecord record = data.get(i);
            line.getData().add(new XYChart.Data<>(record.getPointNumber(), fullReaction ? record.getFullReaction() : record.getDynamicReaction()));
        }
        return line;
    }
}
