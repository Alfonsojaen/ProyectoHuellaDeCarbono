package github.alfonsojaen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class MainApp extends Application {

    public static Scene scene;
    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("pantallaLoginUser"), 640, 480);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static Scene createScene(String fxml, double width, double height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource( "view/" + fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, width, height);
        return scene;
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("view/" +fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}