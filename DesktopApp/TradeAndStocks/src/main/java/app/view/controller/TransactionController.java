package app.view.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class TransactionController {
    @FXML
    private Label nameLabel;
    @FXML
    private TextField units;



    @FXML
    private TextField value;


    @FXML
    protected void writingOnUnits() throws IOException{
        units.clear();
        units.setText(value.getText());
    }

    @FXML
    protected void writingOnValue() throws IOException{
        value.clear();
        value.setText(units.getText());
    }

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
