package ru.stankin;

import javafx.event.ActionEvent;

public class AboutController {
    private static final String REP_URL = "https://github.com/d1slike/labProject";

    public void onClickGithubLink(ActionEvent event) {
        MainApplication.getHServices().showDocument(REP_URL);
    }
}
