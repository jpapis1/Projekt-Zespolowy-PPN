package app.view.window;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import app.view.window.MyStocksWindow;

public class HomeWindow extends Application{
    @Override
    public void start(Stage stage) {


        //Creating Buttons

        Button aboutButton = new Button("ABOUT");
        Button myAccountButton = new Button("MY ACCOUNT");
        Button allStocksButton = new Button("ALL STOCKS");
        Button myStocksButton = new Button("MY STOCKS");
        Button testButton = new Button("Test");
        //Creating a Grid Pane
        GridPane gridPane = new GridPane();

        //Setting size for the pane
        gridPane.setMinSize(1500, 750);

        //Setting the padding
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        //Arranging all the nodes in the grid
        gridPane.add(allStocksButton, 1, 0);
        gridPane.add(myStocksButton, 2, 0);
        gridPane.add(myAccountButton, 3, 0);
        gridPane.add(aboutButton, 4, 0);
        allStocksButton.setStyle("-fx-background-color: #C5C5C5; -fx-text-fill: white; -fx-font: normal 15px 'sans-serif' ");
        myStocksButton.setStyle("-fx-background-color: #C5C5C5; -fx-text-fill: white; -fx-font: normal 15px 'sans-serif' ");
        myAccountButton.setStyle("-fx-background-color: #C5C5C5; -fx-text-fill: white; -fx-font: normal 15px 'sans-serif' ");
        aboutButton.setStyle("-fx-background-color: #C5C5C5; -fx-text-fill: white; -fx-font: normal 15px 'sans-serif' ");
        gridPane.setStyle("-fx-background-color: #f5f5f5;");


        //test
        GridPane gridPane2 = new GridPane();
        gridPane2.setPadding(new Insets(10, 10, 10, 10));

        //Setting the vertical and horizontal gaps between the columns
        gridPane2.setVgap(5);
        gridPane2.setHgap(5);
        gridPane2.setAlignment(Pos.TOP_LEFT);
        gridPane2.add(testButton, 0, 10);


        //Creating a Group object
        Group root = new Group(gridPane , MyStocksWindow.myStocksPane);

        //Creating a scene object
        Scene scene = new Scene(root, 1500, 750);
        //Setting title to the Stage
        stage.setTitle("Trade and Stocks");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }

}

