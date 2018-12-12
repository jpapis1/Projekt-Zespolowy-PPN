package app.view.window;

import app.service.UserService;
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

public class LoginWindow extends Application {
    @Override
    public void start(Stage stage) {
        //creating label email
        Text emailOrUsername = new Text("Email/Username");

        //creating label password
        Text passwordText = new Text("Password");

        Text wrongPassword = new Text("Wrong Password");

        //Creating Text Filed for email
        TextField emailOrUsernameField = new TextField();

        //Creating Text Filed for password
        PasswordField passwordTextField = new PasswordField();

        //Creating Buttons
        Button submitButton = new Button("Submit");
        Button clearButton = new Button("Clear");
        DropShadow shadow = new DropShadow();


        GridPane gridPane = new GridPane();
        //Adding the shadow when the mouse cursor is on
        submitButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>()
        {
            @Override public void handle(MouseEvent e)
            {
                submitButton.setEffect(shadow);
                submitButton.setStyle("-fx-background-color: #16a6b6; -fx-text-fill: white; -fx-font: normal 15px 'sans-serif' ");
            }
        });
        //Removing the shadow when the mouse cursor is off
        submitButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e)
            {
                submitButton.setStyle("-fx-background-color: #C5C5C5; -fx-text-fill: white; -fx-font: normal 15px 'sans-serif' ");
                submitButton.setEffect(null);
            }
        });
        submitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override public void handle(MouseEvent e)
            {
                submitButton.setEffect(shadow);
                submitButton.setStyle("-fx-background-color: #cb2514; -fx-text-fill: white; -fx-font: normal 15px 'sans-serif' ");
                try {
                    MainWindow main = new MainWindow();
                    main.init();
                    main.start(new Stage());
                    new MainWindow().start(new Stage());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                /*try {
                    if (UserService.isPasswordCorrect(emailOrUsernameField.getText(), passwordTextField.getText())) {
                        stage.hide();
                        //new HomeWindow().start(new Stage());
                        //new MyStocksWindow().start(new Stage());
                        new MainWindow().start(new Stage());
                    } else {
                        gridPane.add(wrongPassword, 1, 3);
                    }
                }
                catch(Exception exc)
                {

                    wrongPassword.setStyle("-fx-background-color:#f5f5f5; -fx-font: normal 15px 'sans-serif';-fx-text-fill: red");
                    gridPane.add(wrongPassword, 1, 3);
                }
                */
            }
        });

        clearButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>()
        {
            @Override public void handle(MouseEvent e)
            {
                clearButton.setEffect(shadow);
                clearButton.setStyle("-fx-background-color: #16a6b6; -fx-text-fill: white; -fx-font: normal 15px 'sans-serif' ");
            }
        });
        //Removing the shadow when the mouse cursor is off
        clearButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e)
            {
                clearButton.setStyle("-fx-background-color: #C5C5C5; -fx-text-fill: white; -fx-font: normal 15px 'sans-serif' ");
                clearButton.setEffect(null);
            }
        });

        clearButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
        {
            @Override public void handle(MouseEvent e)
            {
                clearButton.setEffect(shadow);
                clearButton.setStyle("-fx-background-color: #cb2514; -fx-text-fill: white; -fx-font: normal 15px 'sans-serif' ");
                emailOrUsernameField.clear();
                passwordTextField.clear();
            }
        });


        //Setting size for the pane
        gridPane.setMinSize(2000, 1000);

        //Setting the padding
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        //Setting the Grid alignment
        gridPane.setAlignment(Pos.CENTER);

        //Arranging all the nodes in the grid
        gridPane.add(emailOrUsername, 0, 0);
        gridPane.add(emailOrUsernameField, 1, 0);
        gridPane.add(passwordText, 0, 1);
        gridPane.add(passwordTextField, 1, 1);
        gridPane.add(submitButton, 0, 2);
        gridPane.add(clearButton, 1, 2);

        //Styling nodes
        submitButton.setStyle("-fx-background-color: #C5C5C5; -fx-text-fill: white; -fx-font: normal 15px 'sans-serif'");
        clearButton.setStyle("-fx-background-color: #C5C5C5; -fx-text-fill: white; -fx-font: normal 15px 'sans-serif'");

        emailOrUsername.setStyle("-fx-font: normal 15px 'sans-serif' ");
        passwordText.setStyle("-fx-font: normal 15px 'sans-serif' ");
        gridPane.setStyle("-fx-background-color: #f5f5f5;");

        //Creating a scene object
        Scene scene = new Scene(gridPane);

        //Setting title to the Stage
        stage.setTitle("Login");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }
}