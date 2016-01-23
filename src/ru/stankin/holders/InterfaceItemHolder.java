package ru.stankin.holders;

import javafx.scene.control.Control;
import ru.stankin.model.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dislike on 23.01.2016.
 */
public class InterfaceItemHolder {
    private final Map<String, Control> itemReferenceMap;

    public InterfaceItemHolder(Class interfaceControllerClass) {
        itemReferenceMap = new HashMap<>();
    }

    public void putItem(String name, Control control) {
        itemReferenceMap.put(name, control);
    }

    private void disableAll() {
        itemReferenceMap.values().forEach(item -> item.setDisable(true));
    }

    public void prepareInterfaceForCurrentStage(Stage currentStage) {
        disableAll();
        for (String itemName : currentStage.getAllowedUiItemsOnStage())
            itemReferenceMap.get(itemName).setDisable(false);
    }
}
