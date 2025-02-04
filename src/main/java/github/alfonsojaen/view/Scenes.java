package github.alfonsojaen.view;

import github.alfonsojaen.MainApp;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class Scenes {
    public static void setRoot(String fxml) throws IOException {
        Parent p = MainApp.loadFXML(fxml);
        Scene newScene;
        if (fxml.equals("pantallaLoginUser")) {
            newScene = MainApp.createScene(fxml, 640, 420);
        } else if (fxml.equals("pantallaRegisterUser")) {
            newScene = MainApp.createScene(fxml, 640, 420);
        } else if (fxml.equals("pantallaMenu")) {
            newScene = MainApp.createScene(fxml, 640, 420);
        } else if (fxml.equals("pantallaInsertHuella")) {
            newScene = MainApp.createScene(fxml, 600, 400);
        } else if (fxml.equals("pantallaInsertHabito")) {
            newScene = MainApp.createScene(fxml, 600, 400);
        } else if (fxml.equals("pantallaHabitos")) {
            newScene = MainApp.createScene(fxml, 640, 420);
        } else if (fxml.equals("pantallaHuellas")) {
            newScene = MainApp.createScene(fxml, 640, 420);
        } else if (fxml.equals("pantallaUpdateUser")) {
            newScene = MainApp.createScene(fxml, 640, 420);
    } else {
            newScene = MainApp.createScene(fxml, 600, 400);
        }
        MainApp.primaryStage.setScene(newScene);
        MainApp.scene.setRoot(p);


    }
}

