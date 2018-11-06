package app.view;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RegisterWindow extends Application {
    @Override
    public void start(Stage stage) {

        Text usernameLabel = new Text("USERNAME");

        TextField usernameText = new TextField();

        Text passwordLabel = new Text("PASSWORD");

        TextField passwordText = new TextField();

        Text emailLabel = new Text("E-MAIL");

        TextField emailText = new TextField();

        //Label for name
        Text firstNameLabel = new Text("FIRST NAME");

        //Text field for name
        TextField firstNameText = new TextField();

        Text lastNameLabel = new Text("LAST NAME");

        TextField lastNameText = new TextField();

        Text taxRateLabel = new Text("TAX RATE");

        TextField taxRateText = new TextField();

        Text brokersProfitMarginLabel = new Text("BROKERS PROFIT MARGIN");

        TextField brokersProfitMarginText = new TextField();

        Text handlingFeeLabel = new Text("HANDLING FEE");

        TextField handlingFeeText = new TextField();

        //Label for date of birth
        Text dobLabel = new Text("DATE OF BIRTH");

        //date picker to choose date
        DatePicker datePicker = new DatePicker();

        //Label for gender
        Text genderLabel = new Text("GENDER");

        //Toggle group of radio buttons
        ToggleGroup groupGender = new ToggleGroup();
        RadioButton maleRadio = new RadioButton("MALE");
        maleRadio.setToggleGroup(groupGender);
        RadioButton femaleRadio = new RadioButton("FEMALE");
        femaleRadio.setToggleGroup(groupGender);

        //Label for register
        Button buttonRegister = new Button("REGISTER");
        DropShadow shadow = new DropShadow();

        //Adding the shadow when the mouse cursor is on
        buttonRegister.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>()
        {
            @Override public void handle(MouseEvent e)
            {
                buttonRegister.setEffect(shadow);
                buttonRegister.setStyle("-fx-background-color: #16a6b6; -fx-text-fill: white; -fx-font: normal 15px 'sans-serif' ");
            }
        });
        //Removing the shadow when the mouse cursor is off
        buttonRegister.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e)
            {
                buttonRegister.setStyle("-fx-background-color: #C5C5C5; -fx-text-fill: white; -fx-font: normal 15px 'sans-serif' ");
                buttonRegister.setEffect(null);
            }
        });
        //Creating a Grid Pane
        GridPane gridPane = new GridPane();

        //Setting size for the pane
        gridPane.setMinSize(800, 500);

        //Setting the padding
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        //Setting the Grid alignment
        gridPane.setAlignment(Pos.CENTER);

        //Arranging all the nodes in the grid
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameText, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordText, 1, 1);
        gridPane.add(emailLabel, 0, 2);
        gridPane.add(emailText, 1, 2);
        gridPane.add(firstNameLabel, 0, 3);
        gridPane.add(firstNameText, 1, 3);
        gridPane.add(lastNameLabel, 0, 4);
        gridPane.add(lastNameText, 1, 4);
        gridPane.add(taxRateLabel, 0, 5);
        gridPane.add(taxRateText, 1, 5);
        gridPane.add(brokersProfitMarginLabel, 0, 6);
        gridPane.add(brokersProfitMarginText, 1, 6);
        gridPane.add(handlingFeeLabel, 0, 7);
        gridPane.add(handlingFeeText, 1, 7);
        gridPane.add(dobLabel, 0, 8);
        gridPane.add(datePicker, 1, 8);

        gridPane.add(genderLabel, 0, 9);
        gridPane.add(maleRadio, 1, 9);
        gridPane.add(femaleRadio, 2, 9);

        gridPane.add(buttonRegister, 2, 12);

        //Styling nodes

        buttonRegister.setStyle("-fx-background-color: #C5C5C5; -fx-text-fill: white; -fx-font: normal 15px 'sans-serif' ");
        usernameLabel.setStyle("-fx-font: normal 15px 'sans-serif' ");
        passwordLabel.setStyle("-fx-font: normal 15px 'sans-serif' ");
        emailLabel.setStyle("-fx-font: normal 15px 'sans-serif' ");
        firstNameLabel.setStyle("-fx-font: normal 15px 'sans-serif' ");
        lastNameLabel.setStyle("-fx-font: normal 15px 'sans-serif' ");
        dobLabel.setStyle("-fx-font: normal 15px 'sans-serif' ");
        genderLabel.setStyle("-fx-font: normal 15px 'sans-serif' ");
        taxRateLabel.setStyle("-fx-font: normal 15px 'sans-serif' ");
        brokersProfitMarginLabel.setStyle("-fx-font: normal 15px 'sans-serif' ");
        handlingFeeLabel.setStyle("-fx-font: normal 15px 'sans-serif' ");
        //Setting the back ground color
        gridPane.setStyle("-fx-background-color: #f5f5f5;");

        //Creating a scene object
        Scene scene = new Scene(gridPane);

        //Setting title to the Stage
        stage.setTitle("Registration Form");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }
    public static void main(String args[]){
        launch(args);
    }
}