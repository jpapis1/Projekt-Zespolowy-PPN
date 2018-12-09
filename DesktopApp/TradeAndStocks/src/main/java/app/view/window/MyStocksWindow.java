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
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.Date;

public class MyStocksWindow extends Application {
    private TableView allStocksTable = new TableView();
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    //Creating Buttons
    public void start(Stage stage) {
        Button profileButton = new Button("   PROFILE   ");
        Button resetButton = new Button("RESET ACCOUNT");

        //Creating a Grid Pane
        GridPane gridPane = new GridPane();

        //Setting size for the pane
        gridPane.setMinSize(2000, 1000);

        //Setting the padding
        gridPane.setPadding(new Insets(10, 0, 0, 10));

        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        //Arranging all the nodes in the grid
        gridPane.add(profileButton, 1, 0);
        gridPane.add(resetButton, 2, 0);
        gridPane.add(allStocksTable,3,1);
        profileButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        resetButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        gridPane.setStyle("-fx-background-color: BEIGE;");


        TableColumn <AllStocksTable, String> shortNameColumn = new TableColumn("shortName");
        TableColumn <AllStocksTable, String> nameColumn = new TableColumn("name");
        TableColumn <AllStocksTable, String> sectorColumn = new TableColumn("sector");
        TableColumn <AllStocksTable, Date> dateColumn = new TableColumn("date");
        TableColumn <AllStocksTable, Double> priceColumn = new TableColumn("price");
        ArrayList<AllStocksTable> data = StockDataService.getAllStocksTableList();
        ObservableList<AllStocksTable> observableData = FXCollections.observableArrayList(data);

        sectorColumn.setCellValueFactory(new PropertyValueFactory<>("sector"));
        shortNameColumn.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        allStocksTable.setItems(observableData);



        allStocksTable.getColumns().addAll(shortNameColumn, nameColumn,priceColumn, dateColumn,sectorColumn);
        allStocksTable.setPrefSize(800,500);
        //Creating a Group object
        Group root = new Group(gridPane);

        //Creating a scene object
        Scene scene = new Scene(root, 2000, 1000);
        //Setting title to the Stage
        stage.setTitle("My Stocks");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }
}