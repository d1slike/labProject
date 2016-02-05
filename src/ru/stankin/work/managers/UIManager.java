package ru.stankin.work.managers;

import javafx.scene.control.Control;

import ru.stankin.enums.WorkStage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dislike on 23.01.2016.
 */
public class UIManager {

    public static final String RED_BORDER_STYLE = "-fx-border-color: red; -fx-border-width: 1;";
    public static final String DEFAULT_BORDER_STYLE = "";

    private final Map<String, Control> itemReferenceMap;
    private final List<String> notDefaultStyleItems;


    public UIManager() {
        itemReferenceMap = new HashMap<>();
        notDefaultStyleItems = new ArrayList<>();
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
    }

    public void setRedBorder(String elementName) {
        itemReferenceMap.get(elementName).setStyle(RED_BORDER_STYLE);
        notDefaultStyleItems.add(elementName);
    }
}
