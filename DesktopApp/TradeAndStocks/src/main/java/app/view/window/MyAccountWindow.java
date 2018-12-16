package app.view.window;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MyAccountWindow extends Application {
    public static Scene myAccountScene2;

    public static Scene getScene() {
        return myAccountScene2;
    }

    @Override
    public void start(Stage homeStage) {
        Button homeButton = new Button("Home");
        new MyStocksWindow().start(new Stage());
        homeButton.setOnAction(event -> homeStage.setScene(HomeWindow.getScene()));
        StackPane myAccountLayout = new StackPane();
        myAccountLayout.getChildren().add(homeButton);
        Scene myAccountScene = new Scene(myAccountLayout, 200, 200);
        myAccountScene2 = myAccountScene;
    }

}
