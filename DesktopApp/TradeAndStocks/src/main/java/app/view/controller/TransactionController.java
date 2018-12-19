package app.view.controller;

import app.api.StockData;
import app.service.TransactionService;
import app.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class TransactionController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label unitPrice;
    @FXML
    private TextField units;



    @FXML
    private TextField value;

    @Autowired
    UserService userService;

    @FXML
    protected void writingOnUnits() throws IOException{

        try
        {
            System.out.println(Double.parseDouble(units.getText()));
            Double producedValue = Double.parseDouble(unitPrice.getText())* Double.parseDouble(units.getText());
            value.setText(producedValue.toString());
        }
        catch(NumberFormatException e)
        {
            value.setText("ERROR");
        }

    }

    @FXML
    protected void writingOnValue() throws IOException{

        try
        {
            Double producedUnits = Double.parseDouble(value.getText()) / Double.parseDouble(unitPrice.getText());
            units.setText(producedUnits.toString());
        }
        catch(NumberFormatException e)
        {
            units.setText("ERROR");
        }
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
    public void setUnitPrice(Double price) {
        unitPrice.setText(price.toString());
    }


}
