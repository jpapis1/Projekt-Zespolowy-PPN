package app.view.windows;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomeWindow extends Application{
    @Override
    public void start(Stage stage) {
        //Creating a Polygon
        Polygon hexagon = new Polygon();
        Polygon hexagon2 = new Polygon();
        Polygon hexagon3 = new Polygon();
        Polygon hexagon4 = new Polygon();
        Polygon hexagon5 = new Polygon();
        Polygon hexagon6 = new Polygon();
        Polygon hexagon7 = new Polygon();


        //Adding coordinates to the polygon
        hexagon.getPoints().addAll(new Double[]{
                425.0, 273.0,
                263.0, 367.0,
                263.0, 554.0,
                425.0, 648.0,
                587.0, 554.0,
                587.0, 367.0,
        });

        hexagon2.getPoints().addAll(new Double[]{
                775.0, 273.0,
                613.0, 367.0,
                613.0, 554.0,
                775.0, 648.0,
                937.0, 554.0,
                937.0, 367.0,
        });

        hexagon3.getPoints().addAll(new Double[]{
                1125.0, 273.0,
                963.0, 367.0,
                963.0, 554.0,
                1125.0, 648.0,
                1287.0, 554.0,
                1287.0, 367.0,
        });

        hexagon4.getPoints().addAll(new Double[]{
                1475.0, 273.0,
                1313.0, 367.0,
                1313.0, 554.0,
                1475.0, 648.0,
                1637.0, 554.0,
                1637.0, 367.0,
        });

        hexagon5.getPoints().addAll(new Double[]{
                600.0, 578.0,
                438.0, 672.0,
                438.0, 859.0,
                600.0, 952.0,
                762.0, 859.0,
                762.0, 672.0,
        });

        hexagon6.getPoints().addAll(new Double[]{
                950.0, 578.0,
                788.0, 672.0,
                788.0, 859.0,
                950.0, 952.0,
                1112.0, 859.0,
                1112.0, 672.0,
        });

        hexagon7.getPoints().addAll(new Double[]{
                1300.0, 578.0,
                1138.0, 672.0,
                1138.0, 859.0,
                1300.0, 952.0,
                1462.0, 859.0,
                1462.0, 672.0,
        });


        //Creating Buttons
        Button profileButton = new Button("   PROFILE   ");
        Button button2 = new Button("RESET ACCOUNT");
        Button menuButton1 = new Button();

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
        gridPane.add(button2, 2, 0);
        profileButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        button2.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        menuButton1.setStyle("-fx-shape:M10 10;");
        gridPane.setStyle("-fx-background-color: BEIGE;");




        //Creating a Group object
        Group root = new Group(gridPane,menuButton1,hexagon,hexagon2,hexagon3,hexagon4,hexagon5,hexagon6,hexagon7);

        //Creating a scene object
        Scene scene = new Scene(root, 2000, 1000);
        //Setting title to the Stage
        stage.setTitle("Home Page");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }

}

