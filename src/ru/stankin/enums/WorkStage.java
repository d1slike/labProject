package ru.stankin.enums;

/**
 * Created by Dislike on 23.01.2016.
 */
public enum WorkStage {


    STAGE_1_SELECT_ALT_VAR("Выберите изменяемый параметр в соответствии с вашим вариантом", AllowedItemsSpace.STAGE_1_ALLOWED_UI_ITEMS, ElementNames.COMBO_BOX_ALT_VAR_SWITCHER),
    STAGE_2_WRITE_STEP_TO_ALT_VAR("Введите шаг изменяемого параметра", AllowedItemsSpace.STAGE_2_ALLOWED_UI_ITEMS, ElementNames.FIELD_ALT_VAR_STEP),
    STAGE_3_FILL_VAR_TABLE("Заполните таблицу переменных в соответствии с вашим вариантом", AllowedItemsSpace.STAGE_3_ALLOWED_UI_ITEMS, ElementNames.TABLE_VARIABLES),
    STAGE_4_SELECT_RESEARCH_VAR("Выберите исследуемую реакцию в соответствии с вашим вариантом", AllowedItemsSpace.STAGE_4_ALLOWED_UI_ITEMS, ElementNames.COMBO_BOX_RESEARCH_VAR_SWITCHER),
    STAGE_5_WRITE_TIME_STEP("Для вычисленного значения оборотов в минуту(N) определите значение " + VariableType.VarName.DELTA + "t", AllowedItemsSpace.STAGE_5_ALLOWED_UI_ITEMS, ElementNames.FIELD_TIME),
    STAGE_6_CHECK_CHART("Скопируйте графики и определите зависимости исслеудемой реакции от изменяемого параметра", AllowedItemsSpace.STAGE_6_ALLOWED_UI_ITEMS, ElementNames.BUTTON_SHOW_CHART);


    private final String description;
    private final String[] allowedUiItemsOnStage;
    private final String selectableElement;

    WorkStage(String description, String[] allowedUiItemsOnStage, String selectableElement) {
        this.description = description;
        this.allowedUiItemsOnStage = allowedUiItemsOnStage;
        this.selectableElement = selectableElement;
    }

    public WorkStage nextStage() {
        return ordinal() == values().length - 1 ? this : values()[ordinal() + 1];
    }

    public WorkStage prevStage() {
        return ordinal() == 0 ? this : values()[ordinal() - 1];
    }

    public String getDescription() {
        return description;
    }

    public String[] getAllowedUiItemsOnStage() {
        return allowedUiItemsOnStage;
    }

    public String getSelectableElement() {
        return selectableElement;
    }

    private static final class AllowedItemsSpace {
        private static final String[] STAGE_1_ALLOWED_UI_ITEMS = {ElementNames.COMBO_BOX_ALT_VAR_SWITCHER, ElementNames.BUTTON_NEXT_STAGE};
        private static final String[] STAGE_2_ALLOWED_UI_ITEMS = {ElementNames.BUTTON_PREV_STAGE, ElementNames.BUTTON_NEXT_STAGE, ElementNames.FIELD_ALT_VAR_STEP};
        private static final String[] STAGE_3_ALLOWED_UI_ITEMS = {ElementNames.BUTTON_NEXT_STAGE, ElementNames.BUTTON_PREV_STAGE, ElementNames.TABLE_VARIABLES};
        private static final String[] STAGE_4_ALLOWED_UI_ITEMS = {ElementNames.BUTTON_NEXT_STAGE, ElementNames.BUTTON_PREV_STAGE, ElementNames.COMBO_BOX_RESEARCH_VAR_SWITCHER};
        private static final String[] STAGE_5_ALLOWED_UI_ITEMS = {ElementNames.FIELD_TIME, ElementNames.TABLE_RESULTS, ElementNames.BUTTON_CALCULATE, ElementNames.BUTTON_PREV_STAGE};
        private static final String[] STAGE_6_ALLOWED_UI_ITEMS = {ElementNames.TABLE_RESULTS, ElementNames.BUTTON_SHOW_CHART, ElementNames.BUTTON_CANCEL};
        //private static final String[] STAGE_7_ALLOWED_UI_ITEMS = {};
    }
}

