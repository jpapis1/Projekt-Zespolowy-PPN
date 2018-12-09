package app.view.window;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FinancialAnalysisWindow extends Application {
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
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        //Arranging all the nodes in the grid
        gridPane.add(profileButton, 1, 0);
        gridPane.add(resetButton, 2, 0);
        profileButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        resetButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        gridPane.setStyle("-fx-background-color: BEIGE;");


        //Creating a Group object
        Group root = new Group(gridPane);

        //Creating a scene object
        Scene scene = new Scene(root, 2000, 1000);
        //Setting title to the Stage
        stage.setTitle("Financial Analysis");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }
}