package ru.stankin;

/**
 * Created by DisDev on 05.02.2016.
 */
public abstract class AbstractController {
    private MainApplication mainApplication;

    public final MainApplication getMainApplication() {
        return mainApplication;
    }

    public final void setMainApplication(MainApplication mainApplication) {
        this.mainApplication = mainApplication;
    }
}
