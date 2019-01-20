package app.view.controller;

import app.api.StockDataService;
import app.model.Broker;
import app.model.User;
import app.service.BrokerService;
import app.service.TransactionService;
import app.service.UserService;
import app.view.table.AllStocksTable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Controller
public class MyProfileController implements Initializable {

    @Autowired
    BrokerService brokerService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    UserService userService;
/*
    @FXML
    public PasswordField oldPasswordTextField;
    @FXML
    public PasswordField newPasswordTextField;
    @FXML
    public Label passwordStatusLabel;
    */
    @FXML
    private ComboBox<Broker> brokerComboBox;

    @FXML
    private Label profitMarginLabel;
    @FXML
    private Label handlingFeeLabel;
    @FXML
    private Label countryLabel;
    @FXML
    private Button resetButton;
    @FXML
    private TextField balanceTextField;
    @FXML
    protected void updateBrokerSelection(ActionEvent event) throws IOException {

        profitMarginLabel.setText("Profit margin: " + brokerComboBox.getValue().getProfitMargin()*100 + "%");
        handlingFeeLabel.setText("Handling fee: " + brokerComboBox.getValue().getHandlingFee()*100 + "%");
        countryLabel.setText("Country: " + brokerComboBox.getValue().getCountry().getName() + " with " +
                brokerComboBox.getValue().getCountry().getTaxRate()*100 + "% tax rate");
        resetButton.setDisable(false);


    }

    @FXML
    protected void resetAccount(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Reset confirmation");
        alert.setContentText("Are you sure you want to reset the account?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            transactionService.clearUsersTransactions(UserService.getActiveUser());
            System.out.println("clear transactions ok");
            UserService.getActiveUser().setFunds(Double.parseDouble(balanceTextField.getText()));
            System.out.println("set funds ok");
            UserService.getActiveUser().setBroker(brokerComboBox.getValue());
            System.out.println("set broker ok");
            userService.updateUser(UserService.getActiveUser());
            System.out.println("update user ok");
            User user = UserService.getActiveUser();
            MenuController.infoLabelP.setText("Current balance: $" + String.valueOf(user.getFunds()) +
                    " | Broker: " + user.getBroker() + " | Handling Fee: " + user.getBroker().getHandlingFee()*100 + "%" +
                    " | Profit Margin: " + user.getBroker().getProfitMargin() + "%"+ " | Tax rate: " + user.getBroker().getCountry().getTaxRate()*100 + "%");
            // ... user chose OK
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Information Dialog");
            alert2.setHeaderText(null);
            alert2.setContentText("Your account has been successfully reset with " + brokerComboBox.getValue() + " broker and balance of $" + balanceTextField.getText() + "!");

            alert2.showAndWait();


            try {
                Parent menuParent = FXMLLoader.load(getClass().getResource("/fxml/client/window/menu.fxml"));
                Scene menu = new Scene(menuParent);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(menu);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR");
            }


        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(brokerService);
        List<Broker> allBrokers = brokerService.getAllBrokers();
        ObservableList<Broker> observableData = FXCollections.observableArrayList(allBrokers);
        brokerComboBox.setItems(observableData);

        balanceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                balanceTextField.setText(oldValue);
            }
        });
        /*
        sector.setCellValueFactory(new PropertyValueFactory<>("sector"));
        shortName.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        ArrayList<AllStocksTable> data = new ArrayList<>();
        System.out.println("LOADING");
        if(loadedList.size() == 0) {
            data = StockDataService.getAllStocksTableList();
            loadedList = data;

        }  else {
            data = loadedList;
        }
        ObservableList<AllStocksTable> observableData = FXCollections.observableArrayList(data);
        allStockTableView.setItems(observableData);*/



    }

    /*public void changePassword(ActionEvent actionEvent) {
        User myUser = UserService.getActiveUser();

        if(userService.isPasswordCorrect(myUser.getUsername(),oldPasswordTextField.getText())) {

            myUser.setPassword(userService.hashPassword(newPasswordTextField.getText()));
            userService.updateUser(myUser);
            passwordStatusLabel.setText("Password has been reset!");
        } else {
            passwordStatusLabel.setText("Old password doesn't match!");
        }
    }*/
}
