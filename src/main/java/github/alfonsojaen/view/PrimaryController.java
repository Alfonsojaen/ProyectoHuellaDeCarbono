package github.alfonsojaen.view;

import java.io.IOException;

import github.alfonsojaen.MainApp;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        Scenes.setRoot("secondary");
    }
}
