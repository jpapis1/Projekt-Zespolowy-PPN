package app.view.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TransactionController {
    @FXML
    protected void handleBackButtonAction(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/fxml/window/menu.fxml"));
        Scene menu = new Scene(loginParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(menu);
        stage.show();
    }
}
