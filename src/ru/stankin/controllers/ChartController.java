package ru.stankin.controllers;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.stankin.model.ResultRecord;

/**
 * Created by DisDev on 27.01.2016.
 */
public class ChartController {
    private final NumberAxis yAxis;
    private final LineChart<Number, Number> chart;
    private final Stage stage;

    private boolean alreadyBuilt;

    public ChartController(Stage primaryStage) {
        yAxis = new NumberAxis();
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Время(t)");

        chart = new LineChart<>(xAxis, yAxis);

        BorderPane pane = new BorderPane(chart);
        Scene scene = new Scene(pane);
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);
        stage.setTitle("График");
        stage.setIconified(false);
        stage.setHeight(400);
        stage.setWidth(600);
        stage.setResizable(false);
        stage.setScene(scene);
    }

    public void buildAndShow(ObservableList<ResultRecord> resultRecords, String reseacrhValueName, final int altVarStepsCount) {

        if (!alreadyBuilt) {

            yAxis.setLabel(reseacrhValueName);

            int currentPos = 0;

            XYChart.Series<Number, Number> firstLine = new XYChart.Series<>();
            for (; currentPos < altVarStepsCount; currentPos++) {
                ResultRecord record = resultRecords.get(currentPos);
                firstLine.getData().add(new XYChart.Data<>(record.getTime(), record.getFullReaction()));
            }
            XYChart.Series<Number, Number> secondLine = new XYChart.Series<>();
            for (; currentPos < altVarStepsCount * 2; currentPos++) {
                ResultRecord record = resultRecords.get(currentPos);
                secondLine.getData().add(new XYChart.Data<>(record.getTime(), record.getFullReaction()));
            }
            XYChart.Series<Number, Number> thirdLine = new XYChart.Series<>();
            for (; currentPos < altVarStepsCount * 3; currentPos++) {
                ResultRecord record = resultRecords.get(currentPos);
                thirdLine.getData().add(new XYChart.Data<>(record.getTime(), record.getFullReaction()));
            }
            XYChart.Series<Number, Number> fourthLine = new XYChart.Series<>();
            for (; currentPos < altVarStepsCount * 4; currentPos++) {
                ResultRecord record = resultRecords.get(currentPos);
                fourthLine.getData().add(new XYChart.Data<>(record.getTime(), record.getFullReaction()));
            }

            chart.getData().addAll(firstLine, secondLine, thirdLine, fourthLine);
            alreadyBuilt = true;
        }
        stage.showAndWait();
    }

    public void clear() {
        if(alreadyBuilt) {
            chart.getData().forEach(numberNumberSeries -> numberNumberSeries.getData().clear());
            chart.getData().clear();
            alreadyBuilt = false;
        }
    }
}
