package app.view.window;
import app.config.Config;
import app.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LoginController {
    @Autowired
    UserService userService;

    @FXML private Text actiontarget;
    @FXML TextField loginOrPasswordField;
    @FXML TextField passwordField;
    @FXML protected void handleSubmitButtonAction(ActionEvent event) throws IOException {
        actiontarget.setText("You are logging in");
        if(userService.isPasswordCorrect(loginOrPasswordField.getText(),passwordField.getText())) {
            Parent loginParent = FXMLLoader.load(getClass().getResource("/fxml/menu.fxml"));
            Scene menu = new Scene(loginParent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(menu);
            stage.show();
        } else {
            actiontarget.setText("Wrong credentials!");
        }
    }
    @FXML private Text ppn;

    @FXML protected void aboutAction(ActionEvent event) {
        ppn.setText("PAPIS PIĄTEK NAPIERAŁA");
    }


}
