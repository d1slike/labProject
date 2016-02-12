package ru.stankin.work.managers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Control;

import javafx.util.Duration;
import ru.stankin.enums.WorkStage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Dislike on 23.01.2016.
 */
public class UIManager {

    public static final String RED_BORDER_STYLE = "-fx-border-color: red; -fx-border-width: 1;";
    public static final String DEFAULT_BORDER_STYLE = "";
    private static final String SELECTION_BORDER_STYLE = "-fx-border-color: blue; -fx-border-width: 2;";

    private final Map<String, Control> itemReferenceMap;
    private final List<String> notDefaultStyleItems;
    private Timeline activeAnimation;
    private AtomicBoolean activeState;
    private Control activeControl;


    public UIManager() {
        itemReferenceMap = new HashMap<>();
        notDefaultStyleItems = new ArrayList<>();
        activeState = new AtomicBoolean(false);
    }

    public void putItem(String name, Control control) {
        itemReferenceMap.put(name, control);
    }

    private void disableAll() {
        itemReferenceMap.values().forEach(item -> item.setDisable(true));
    }

    public void prepareInterfaceForCurrentStage(WorkStage currentWorkStage) {
        disableAll();
        notDefaultStyleItems.forEach(name -> itemReferenceMap.get(name).setStyle(DEFAULT_BORDER_STYLE));
        notDefaultStyleItems.clear();
        for (String itemName : currentWorkStage.getAllowedUiItemsOnStage())
            itemReferenceMap.get(itemName).setDisable(false);
        playAnimationFor(currentWorkStage.getSelectableElement());
    }

    public void setRedBorder(String elementName) {
        itemReferenceMap.get(elementName).setStyle(RED_BORDER_STYLE);
        notDefaultStyleItems.add(elementName);
    }

    public void playAnimationFor(String elementName) {
        stopAnimation();
        activeControl = itemReferenceMap.getOrDefault(elementName, null);
        if (activeControl == null)
            return;
        activeAnimation = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            activeControl.setStyle(activeState.get() ? DEFAULT_BORDER_STYLE : SELECTION_BORDER_STYLE);
            activeState.set(!activeState.get());
        }));
        activeAnimation.setCycleCount(6);
        activeAnimation.play();
    }

    private void stopAnimation() {
        if (activeAnimation != null) {
            activeAnimation.stop();
            activeAnimation = null;
        }
        if (activeControl != null) {
            activeControl.setStyle(DEFAULT_BORDER_STYLE);
            activeControl = null;
        }
        activeState.set(false);

    }
}
