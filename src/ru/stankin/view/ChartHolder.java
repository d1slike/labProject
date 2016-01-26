package ru.stankin.view;

import javafx.beans.value.ObservableListValue;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.stankin.model.ResultRecord;

/**
 * Created by DisDev on 26.01.2016.
 */
public class ChartHolder {
    private static ChartHolder ourInstance;

    public static ChartHolder getInstance() {
        return ourInstance;
    }

    public static void initChart(ObservableList<ResultRecord> tableRecords, String researchVarName, final int altVarChangedCount) {
        ourInstance = new ChartHolder(tableRecords, researchVarName, altVarChangedCount);
    }

    //private BorderPane pane;
    private Stage stage;
    //private NumberAxis XAxis;
    //private NumberAxis YAxis;
    private LineChart<Number, Number> chart;

    private ChartHolder(ObservableList<ResultRecord> tableRecords, String researchVarName, final int altVarChangedCount) {
        XAxis = new NumberAxis();
        XAxis.setLabel("Время (t)");

        YAxis = new NumberAxis();
        YAxis.setLabel(researchVarName);

        chart = new LineChart<>(XAxis, YAxis);

        int currentPos = 0;

        XYChart.Series<Number, Number> firstLine = new XYChart.Series<>();
        for (; currentPos < altVarChangedCount; currentPos++) {
            ResultRecord record = tableRecords.get(currentPos);
            firstLine.getData().add(new XYChart.Data<>(record.getTime(), record.getFullReaction()));
        }
        XYChart.Series<Number, Number> secondLine = new XYChart.Series<>();
        for (; currentPos < altVarChangedCount * 2; currentPos++) {
            ResultRecord record = tableRecords.get(currentPos);
            secondLine.getData().add(new XYChart.Data<>(record.getTime(), record.getFullReaction()));
        }
        XYChart.Series<Number, Number> thirdLine = new XYChart.Series<>();
        for (; currentPos < altVarChangedCount * 3; currentPos++) {
            ResultRecord record = tableRecords.get(currentPos);
            thirdLine.getData().add(new XYChart.Data<>(record.getTime(), record.getFullReaction()));
        }
        XYChart.Series<Number, Number> fourthLine = new XYChart.Series<>();
        for (; currentPos < altVarChangedCount * 4; currentPos++) {
            ResultRecord record = tableRecords.get(currentPos);
            fourthLine.getData().add(new XYChart.Data<>(record.getTime(), record.getFullReaction()));
        }

        chart.getData().addAll(firstLine, secondLine, thirdLine, fourthLine);

        BorderPane pane = new BorderPane();
        pane.setCenter(chart);
        Scene scene = new Scene(pane);

        stage = new Stage();
        stage.setHeight(400);
        stage.setHeight(600);
        stage.setScene(scene);
        //stage.show();
    }

    public void cleanUp() {
    }

    public void show() {
        stage.show();
    }
}
