package app.view.window;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuWindow extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/window/menu.fxml"));
        Scene scene = new Scene(root, 1000, 750);
        stage.setTitle("Trade and Stocks");
        stage.setScene(scene);
        stage.show();
    }
}
