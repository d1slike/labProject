package ru.stankin.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.stankin.MainApplication;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

/**
 * Created by DisDev on 12.02.2016.
 */
public class Util {
    private static final String RESOURCE_DIR = System.getenv("APP_DIR") == null ? "" : System.getenv("APP_DIR");

    public static CompletableFuture<Image> asyncLoadImageFromResources(String fileName) {
        return CompletableFuture.supplyAsync(() -> MainApplication.class.getResourceAsStream("/" + fileName))
                .thenApply(inputStream -> {
                    try (InputStream image = inputStream) {
                        return new Image(image);
                    } catch (Exception ex) {
                        System.out.printf("Could not load %s, %s\n", fileName, ex.getMessage());
                    }
                    return null;
                });
    }

    public static String externalResource(String filePath) {
        return RESOURCE_DIR + filePath;
    }

    public static String doubleCommaFormat(double value) {
        return String.format("%.3f", value);
    }

    public static String doubleDotFormat(double value) {
        return String.format(Locale.UK, "%.3f", value);
    }

    public static void showMessageAndCloseProgram(String header, String message) {
        Alert alert = prepareErrorAlertWindow();
        alert.setHeaderText(header);
        alert.setContentText(message);
        showAlert(alert);
    }

    public static void showMessageAndCloseProgram(Exception ex) {
        Alert alert = prepareErrorAlertWindow();
        alert.setHeaderText(ex.getMessage());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("StackTrace:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);
        showAlert(alert);
    }

    private static Alert prepareErrorAlertWindow() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(MainApplication.PROGRAM_NAME + " - CriticalError");
        alert.setOnCloseRequest(event -> System.exit(-1));
        alert.initModality(Modality.APPLICATION_MODAL);
        if (MainApplication.getIcon() != null) {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(MainApplication.getIcon());
        }
        return alert;
    }

    private static void showAlert(Alert alert) {
        alert.showAndWait()
                .filter(buttonType -> buttonType == ButtonType.OK)
                .ifPresent(buttonType1 -> System.exit(-1));
    }

    public static void showProgramsFilesSpoiled() {
        showMessageAndCloseProgram(Msg.SPOILED_PROGRAM_FILES, Msg.PLEASE_REINSTALL_AND_RESTART_PROGRAM);
    }

    public static class Msg {
        public static final String SPOILED_PROGRAM_FILES = "Повреждение файлов программы!";
        public static final String PLEASE_REINSTALL_AND_RESTART_PROGRAM = "Пожалуйста переустановите программу и попробуйте запустить повторно.";
    }
}
