package ru.stankin.model;

/**
 * Created by Dislike on 23.01.2016.
 */
public enum Stage {


    STAGE_1_SELECT_ALT_VAR("Выберите изменяемый параметр в соответствии вашему варианту", AllowedItemsSpace.STAGE_1_ALLOWED_UI_ITEMS),
    STAGE_2_WRITE_STEP_TO_ALT_VAR("Введите шаг изменяемого параметра", AllowedItemsSpace.STAGE_2_ALLOWED_UI_ITEMS);


    private final String description;
    private final String[] allowedUiItemsOnStage;

    Stage(String description, String[] allowedUiItemsOnStage) {
        this.description = description;
        this.allowedUiItemsOnStage = allowedUiItemsOnStage;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAllowedUiItemsOnStage() {
        return allowedUiItemsOnStage;
    }

    public Stage nextStage() {
        return ordinal() == values().length - 1 ? this : values()[ordinal() + 1];
    }


    private static final class AllowedItemsSpace {
        private static final String[] STAGE_1_ALLOWED_UI_ITEMS = {"altVarSwitcher", "nextStageButton"};
        private static final String[] STAGE_2_ALLOWED_UI_ITEMS = {"nextStageButton", "altVarStepField"};
        private static final String[] STAGE_3_ALLOWED_UI_ITEMS = {};
        private static final String[] STAGE_4_ALLOWED_UI_ITEMS = {};
        private static final String[] STAGE_5_ALLOWED_UI_ITEMS = {};
        private static final String[] STAGE_6_ALLOWED_UI_ITEMS = {};
        private static final String[] STAGE_7_ALLOWED_UI_ITEMS = {};
    }

}

