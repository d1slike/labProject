package ru.stankin.work.subcontrollers;

import javafx.collections.ObservableList;
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
import java.util.stream.Stream;

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

    public ChartController() {
        yAxisForDynamicReactionsChart = new NumberAxis();
        yAxisForFullReactionsChart = new NumberAxis();

        NumberAxis xAxisForDynamic = new NumberAxis();
        NumberAxis xAsisForFull = new NumberAxis();

        final String xAxisName = "Время(t)";
        xAxisForDynamic.setLabel(xAxisName);
        xAsisForFull.setLabel(xAxisName);

        dynamicReactionsChart = new LineChart<>(xAxisForDynamic, yAxisForDynamicReactionsChart);
        fullReactionsChart = new LineChart<>(xAsisForFull, yAxisForFullReactionsChart);
        dynamicReactionsChart.setPrefSize(CHART_WIDTH, CHART_HEIGHT);
        fullReactionsChart.setPrefSize(CHART_WIDTH, CHART_HEIGHT);

        dynamicReactionsChart.setCreateSymbols(true);
        dynamicReactionsChart.setTitle("Зависимость динамических реакций от времени");
        fullReactionsChart.setCreateSymbols(true);
        fullReactionsChart.setTitle("Зависимость полных реакций от времени");

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
        pane.setPrefSize(CHART_WIDTH * 2, CHART_HEIGHT);
        pane.setLeft(dynamicReactionsChart);
        pane.setRight(fullReactionsChart);


        Scene scene = new Scene(pane);
        stage = new Stage();
        stage.setTitle("Графики");
        stage.setHeight(CHART_HEIGHT);
        stage.setWidth(CHART_WIDTH * 2);
        stage.setIconified(false);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> inShowing = false);
    }

    public void buildAndShow(VariableManager variableManager, Stage primaryStage) {

        if (inShowing)
            return;

        if (!alreadyBuilt) {
            stage.initOwner(primaryStage);
            stage.getIcons().clear();
            stage.getIcons().add(MainApplication.getIcon());
            final String researchVarName = variableManager.getResearchVariable().getName();
            yAxisForDynamicReactionsChart.setLabel(researchVarName + " динамическая");
            yAxisForFullReactionsChart.setLabel(researchVarName + " полная");

            ObservableList<ResultRecord> list = variableManager.getResultRecords();
            String altVarName = variableManager.getAltVariable().getName();
            dynamicReactionsChart.getData().addAll(buildLines(list, altVarName, false));
            fullReactionsChart.getData().addAll(buildLines(list, altVarName, true));
            alreadyBuilt = true;
        }
        inShowing = true;
        stage.showAndWait();
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

        int currentPos = 0;
        int lastTime = maxTimeSteps;

        XYChart.Series<Number, Number> firstLine = new XYChart.Series<>();
        firstLine.setName(altVarName + " = " + Util.doubleCommaFormat(resultRecords.get(currentPos).getAltVar()) + " ");
        for (int i = currentPos; i < lastTime; i++) {
            ResultRecord record = resultRecords.get(i);
            firstLine.getData().add(new XYChart.Data<>(record.getPointNumber(), fullReaction ? record.getFullReaction() : record.getDynamicReaction()));
        }

        currentPos += maxTimeSteps;
        lastTime += maxTimeSteps;

        XYChart.Series<Number, Number> secondLine = new XYChart.Series<>();
        secondLine.setName(altVarName + " = " + Util.doubleCommaFormat(resultRecords.get(currentPos).getAltVar()) + " ");
        for (int i = currentPos; i < lastTime; i++) {
            ResultRecord record = resultRecords.get(i);
            secondLine.getData().add(new XYChart.Data<>(record.getPointNumber(), fullReaction ? record.getFullReaction() : record.getDynamicReaction()));
        }

        currentPos += maxTimeSteps;
        lastTime += maxTimeSteps;

        XYChart.Series<Number, Number> thirdLine = new XYChart.Series<>();
        thirdLine.setName(altVarName + " = " + Util.doubleCommaFormat(resultRecords.get(currentPos).getAltVar()) + " ");
        for (int i = currentPos; i < lastTime; i++) {
            ResultRecord record = resultRecords.get(i);
            thirdLine.getData().add(new XYChart.Data<>(record.getPointNumber(), fullReaction ? record.getFullReaction() : record.getDynamicReaction()));
        }

        currentPos += maxTimeSteps;
        lastTime += maxTimeSteps;

        XYChart.Series<Number, Number> fourthLine = new XYChart.Series<>();
        fourthLine.setName(altVarName + " = " + Util.doubleCommaFormat(resultRecords.get(currentPos).getAltVar()) + " ");
        for (int i = currentPos; i < lastTime; i++) {
            ResultRecord record = resultRecords.get(i);
            fourthLine.getData().add(new XYChart.Data<>(record.getPointNumber(), fullReaction ? record.getFullReaction() : record.getDynamicReaction()));
        }

        List<XYChart.Series<Number, Number>> list = new ArrayList<>();
        Stream.of(firstLine, secondLine, thirdLine, fourthLine).forEach(list::add);
        return list;
    }
}
