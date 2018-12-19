package app.view.controller;

import app.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Controller
public class LoginController {
    @Autowired
    UserService userService;
    @FXML
    TextField loginOrPasswordField;
    @FXML
    TextField passwordField;
    @FXML
    private Text actionTarget;
    @FXML
    private ProgressBar progressBar;
    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) throws IOException {
        progressBar.setProgress(0);
        System.out.println(userService);
        actionTarget.setText("You are logging in");
        if (userService.isPasswordCorrect(loginOrPasswordField.getText(), passwordField.getText())) {
            Parent loginParent = FXMLLoader.load(getClass().getResource("/fxml/window/menu.fxml"));
            progressBar.setProgress(100);
            Scene menu = new Scene(loginParent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(menu);
            stage.show();
        } else {
            actionTarget.setText("Wrong credentials!");
        }
    }


}
