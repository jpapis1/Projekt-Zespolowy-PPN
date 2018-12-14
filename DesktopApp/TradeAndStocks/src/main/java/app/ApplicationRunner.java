package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class ApplicationRunner extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("hello");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainUI.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

    }
    public static void run(String[] args) {
        launch(args);
    }
}