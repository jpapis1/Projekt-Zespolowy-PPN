package app.view.window;

import app.api.StockDataService;
import app.view.table.AllStocksTable;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.Date;

public class MyAccountWindow extends Application{
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
        Scene myAccountScene = new Scene(myAccountLayout, 200,200);
        myAccountScene2 = myAccountScene;
    }

}
