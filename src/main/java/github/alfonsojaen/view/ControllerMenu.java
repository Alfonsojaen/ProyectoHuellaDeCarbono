package github.alfonsojaen.view;

import javafx.fxml.FXML;

import java.io.IOException;

public class ControllerMenu {


    @FXML
    private void switchToShowHabitos() throws IOException {
       Scenes.setRoot("pantallaShowHabitos");
    }

    @FXML
    private void switchToShowHuellas() throws IOException {
        Scenes.setRoot("pantallaShowHuellas");
    }
    @FXML
    private void switchToUserSession() throws IOException {
        Scenes.setRoot("pantallaLoginUser");
    }

}


