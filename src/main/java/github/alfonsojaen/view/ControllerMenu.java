package github.alfonsojaen.view;

import javafx.fxml.FXML;

import java.io.IOException;

public class ControllerMenu {


    @FXML
    private void switchToShowHabitos() throws IOException {
       Scenes.setRoot("pantallaHabitos");
    }

    @FXML
    private void switchToShowHuellas() throws IOException {
        Scenes.setRoot("pantallaHuellas");
    }

    @FXML
    private void switchToUserSession() throws IOException {
        Scenes.setRoot("pantallaLoginUser");
    }

    @FXML
    private void switchToUpdateUser() throws IOException {
        Scenes.setRoot("pantallaUpdateUser");
    }

}


