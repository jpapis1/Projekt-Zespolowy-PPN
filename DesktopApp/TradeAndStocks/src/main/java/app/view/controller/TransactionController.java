package app.view.controller;

import app.api.StockData;
import app.model.Transaction;
import app.service.TransactionService;
import app.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Date;

@Controller
public class TransactionController {
    @FXML
    public RadioButton radioBuy;
    @FXML
    public RadioButton radioSell;
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
    @Autowired
    TransactionService transactionService;

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
    protected void handleBackButtonAction(ActionEvent event) {
        System.out.println("Back button pressed");
        goBack(event);
    }

    public void setNameLabel(String text) {
        nameLabel.setText(text);
    }
    public void setUnitPrice(Double price) {
        unitPrice.setText(price.toString());
    }


    public void handleAcceptButtonAction(ActionEvent actionEvent) {
        Transaction.TransactionBuilder transaction = new Transaction.TransactionBuilder(nameLabel.getText(),UserService.getActiveUser());
        transaction = transaction.date(new Date()).price(Double.parseDouble(unitPrice.getText())).units(Double.parseDouble(units.getText()));
        boolean err = false;
        if(radioBuy.isSelected()) {
            transaction = transaction.setToBuy();
        } else if (radioSell.isSelected()) {
            transaction = transaction.setToSell();
        }
        switch (transactionService.makeTransaction(transaction.build(),radioBuy.isSelected()))
        {
            case Success:
                System.out.println("SUCCESS");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Transaction has been successful!");
                alert.showAndWait();

                break;
            case NotEnoughFunds:
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Information Dialog");
                alert2.setHeaderText(null);
                alert2.setContentText("You have not enough funds!");
                alert2.showAndWait();
                System.out.println("You have not enough funds!");
                break;
            case NothingToSell:
                Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
                alert3.setTitle("Information Dialog");
                alert3.setHeaderText(null);
                alert3.setContentText("You have nothing to sell!");
                alert3.showAndWait();
                break;
        }

    }
    public void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/window/menu.fxml"));
            loader.setControllerFactory(app.Application.app::getBean);
            Parent root = loader.load();

            //Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(root, 800, 400);



            //Parent menuParent = FXMLLoader.load(getClass().getResource("/fxml/window/menu.fxml"));
            //Scene menu = new Scene(menuParent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Trade and Stocks");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR");
        }
    }
}
