package app.view.controller;

import app.api.StockDataService;
import app.model.Transaction;
import app.model.User;
import app.other.Messenger;
import app.service.TransactionService;
import app.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class TransactionController {
    @FXML
    public RadioButton radioBuy;
    @FXML
    public RadioButton radioSell;
    @FXML
    public Label taxRatePercent;
    @FXML
    public Label profitMarginPercent;
    @FXML
    public Label handlingFeePercent;
    @FXML
    public Label totalTransactionValueLabel;


    @FXML
    private Label nameLabel;
    @FXML
    private Label unitPrice;
    @FXML
    private TextField units;

    @FXML
    public Label taxRateValueLabel;
    @FXML
    public Label profitMarginValueLabel;
    @FXML
    public Label handlingFeeValueLabel;

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
            Double producedValue = Double.parseDouble(unitPrice.getText())* Double.parseDouble(units.getText());
            value.setText(producedValue.toString());
            updateFees();
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
            updateFees();

        }
        catch(NumberFormatException e)
        {
            units.setText("ERROR");
        }
    }
    protected void updateFees() {
        System.out.println("UPDATE FEES");
        double profitLoss = 0;
        if(radioBuy.isSelected()) {
            double handlingFeeValue= transactionService.calculateHandlingFee(Double.parseDouble(value.getText()), UserService.getActiveUser().getBroker().getHandlingFee());
            handlingFeeValueLabel.setText(String.format("%.2f",handlingFeeValue));
            handlingFeeValueLabel.setDisable(false);
            taxRateValueLabel.setDisable(true);
            profitMarginValueLabel.setDisable(true);
            profitMarginValueLabel.setText("0.00");
            taxRateValueLabel.setText("0.00");
        } else if (radioSell.isSelected()) {



           // double val = valueBuy - valueSell;
           // double realValue = unitSum * currentUnitPrice;

            profitLoss = transactionService.calculateProfitLoss(nameLabel.getText(),UserService.getActiveUser());
            if(profitLoss>0) {
                User user = UserService.getActiveUser();
                double sellValue = Double.parseDouble(value.getText());
                double profitValue = profitLoss*sellValue;
                double tax = profitValue * user.getBroker().getCountry().getTaxRate();
                taxRateValueLabel.setText(String.format("%.2f", tax));
                double profitMargin = (profitValue - tax) * user.getBroker().getProfitMargin();
                profitMarginValueLabel.setText(String.format("%.2f", profitMargin));
            } else {
                taxRateValueLabel.setText("0.00");
                profitMarginValueLabel.setText("0.00");
            }
            handlingFeeValueLabel.setDisable(true);
            handlingFeeValueLabel.setText("0.00");
            profitMarginValueLabel.setDisable(false);
            taxRateValueLabel.setDisable(false);
            //handlingFeeValueLabel.setText(String.valueOf(Double.parseDouble(unitPrice.getText())*UserService.getActiveUser().getBroker().getHandlingFee()));
        }

        double total = transactionService.calculateTotalValue(radioBuy.isSelected(),
                Double.parseDouble(value.getText()),
                Double.parseDouble(handlingFeeValueLabel.getText()),
                profitLoss,UserService.getActiveUser());

        totalTransactionValueLabel.setText(String.format("%.2f",total));
    }

    @FXML
    protected void handleBackButtonAction(ActionEvent event) {
        goBack(event);
    }

    public void setNameLabel(String text) {
        nameLabel.setText(text);
    }
    public void setUnitPrice(Double price) {
        unitPrice.setText(price.toString());
    }


    public void handleAcceptButtonAction(ActionEvent actionEvent) {
        Transaction.TransactionBuilder transaction = new Transaction.TransactionBuilder(nameLabel.getText(),UserService.getActiveUser())
                .date(new Date()).price(Double.parseDouble(unitPrice.getText())).units(Double.parseDouble(units.getText()));
        if(radioBuy.isSelected()) {
            transaction = transaction.setToBuy();
        } else if (radioSell.isSelected()) {
            transaction = transaction.setToSell();
        }


        switch (transactionService.makeTransaction(transaction.build(),radioBuy.isSelected(),
                Double.parseDouble(totalTransactionValueLabel.getText())))
        {
            case Success:
                Messenger.infoBox("Transaction has been successful!");
                break;
            case NotEnoughFunds:
                Messenger.errorBox("You have not enough funds!");
                break;
            case NothingToSell:
                Messenger.errorBox("You have nothing to sell!");
                break;
        }

    }
    public void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/window/menu.fxml"));
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

    public void radioButtonChanged(ActionEvent actionEvent) {
        updateFees();
    }
}
