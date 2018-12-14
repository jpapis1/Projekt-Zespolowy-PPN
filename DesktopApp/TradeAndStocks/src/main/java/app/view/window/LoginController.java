package app.view.window;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginController {
    @FXML private Text actiontarget;

    @FXML protected void handleSubmitButtonAction(ActionEvent event) throws IOException {

        actiontarget.setText("You are logging in");
        Parent loginParent = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Scene menu = new Scene(loginParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(menu);
        stage.show();
    }
    @FXML private Text ppn;

    @FXML protected void aboutAction(ActionEvent event) {
        ppn.setText("PAPIS PIĄTEK NAPIERAŁA");
    }


}
