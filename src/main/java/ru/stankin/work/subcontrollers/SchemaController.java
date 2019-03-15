package ru.stankin.work.subcontrollers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.stankin.enums.VariableType;
import ru.stankin.utils.Util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SchemaController {
    private static CompletableFuture<Image> SCHEMA_IMAGE;
    private static CompletableFuture<Image> ROLLING_IMAGE;

    public static void prepare() {
        SCHEMA_IMAGE = Util.asyncLoadImageFromResources("gif/schema.jpg");
        ROLLING_IMAGE = Util.asyncLoadImageFromResources("gif/rolling.gif");
    }

    @FXML
    private ImageView schemaImage;
    @FXML
    private ImageView rollingImage;
    @FXML
    private ImageView paramImage;

    @FXML
    private Label paramLabel;

    @FXML
    private void initialize() throws ExecutionException {
        try {
            schemaImage.setImage(SCHEMA_IMAGE.get());
            rollingImage.setImage(ROLLING_IMAGE.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void setParam(VariableType param) {
        paramLabel.setText(param.getName());
        paramImage.setImage(param.getAnimationGif());
    }
}
