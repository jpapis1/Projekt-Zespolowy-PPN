package app.view.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class TransactionController {
    @FXML
    private Label nameLabel;

    @FXML
    protected void handleBackButtonAction(ActionEvent event) throws IOException {
        System.out.println("lolo");
            Parent menuParent = FXMLLoader.load(getClass().getResource("/fxml/window/menu.fxml"));
            Scene menu = new Scene(menuParent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(menu);
            stage.show();
    }
    public void setNameLabel(String text) {
        nameLabel.setText(text);
    }

}
