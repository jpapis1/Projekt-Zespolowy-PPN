package app.view.controller;

import app.api.StockData;
import app.api.StockDataService;
import app.model.Transaction;
import app.model.User;
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
        if(radioBuy.isSelected()) {
            handlingFeeValueLabel.setText(String.format("%.2f",Double.parseDouble(value.getText())*UserService.getActiveUser().getBroker().getHandlingFee()));
            handlingFeeValueLabel.setDisable(false);
            taxRateValueLabel.setDisable(true);
            profitMarginValueLabel.setDisable(true);
            profitMarginValueLabel.setText("0.00");
            taxRateValueLabel.setText("0.00");
        } else if (radioSell.isSelected()) {
            List<Transaction> list = transactionService.getUsersActiveTransactionListOfOneStock(UserService.getActiveUser(),nameLabel.getText());
            double unitSumBuy = list.stream().filter(Transaction::isBuy).mapToDouble(Transaction::getUnits).sum();
            double unitSumSell = list.stream().filter(transaction -> !transaction.isBuy()).mapToDouble(Transaction::getUnits).sum();
            double unitSum = unitSumBuy - unitSumSell;
            double currentUnitPrice = StockDataService.getLatestPrice(nameLabel.getText()).getPrice();
            double valueBuy = list.stream().filter(Transaction::isBuy).mapToDouble(x -> x.getUnitPrice() * x.getUnits()).sum();
            double valueSell = list.stream().filter(transaction -> !transaction.isBuy()).mapToDouble(x -> x.getUnitPrice() * x.getUnits()).sum();
            double val = valueBuy - valueSell;
            double realValue = unitSum * currentUnitPrice;

            double profitLoss = -(1 - (valueSell + (currentUnitPrice*unitSum))/valueBuy);
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

        double total;
        if(radioBuy.isSelected()) {
            total = Double.parseDouble(value.getText()) +
                    Double.parseDouble(handlingFeeValueLabel.getText());
        } else {
            total = Double.parseDouble(value.getText()) -
                    Double.parseDouble(profitMarginValueLabel.getText())
                    -Double.parseDouble(taxRateValueLabel.getText());
        }
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
        Transaction.TransactionBuilder transaction = new Transaction.TransactionBuilder(nameLabel.getText(),UserService.getActiveUser());
        transaction = transaction.date(new Date()).price(Double.parseDouble(unitPrice.getText())).units(Double.parseDouble(units.getText()));
        boolean err = false;
        if(radioBuy.isSelected()) {
            transaction = transaction.setToBuy();
        } else if (radioSell.isSelected()) {
            transaction = transaction.setToSell();
        }
        switch (transactionService.makeTransaction(transaction.build(),radioBuy.isSelected(),
                Double.parseDouble(totalTransactionValueLabel.getText()),Double.parseDouble(units.getText()),Double.parseDouble(unitPrice.getText())))
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
