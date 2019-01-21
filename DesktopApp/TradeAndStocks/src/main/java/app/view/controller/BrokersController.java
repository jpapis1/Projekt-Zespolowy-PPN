package app.view.controller;

import app.model.Broker;
import app.model.Country;
import app.service.BrokerService;
import app.service.CountryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Controller
public class BrokersController implements Initializable {
    public static List<Broker> loadedList = new ArrayList<>();
    @FXML
    public TextField nameTextField;
    @FXML
    public TextField profitMarginTextField;
    @FXML
    public TextField handlingFeeTextField;
    @FXML
    public ComboBox<Country> countryComboBox;
    @Autowired
    BrokerService brokerService;
    @Autowired
    CountryService countryService;

    @FXML
    public TableColumn<Broker,String> name;
    @FXML
    public TableColumn<Broker,Double> profitMargin;
    @FXML
    public TableColumn<Broker,Double> handlingFee;
    @FXML
    public TableColumn<Broker,Country> country;



    @FXML
    public TableView<Broker> brokersTableView;
    public void refreshAction(ActionEvent actionEvent) {
        refreshTable();
        refreshComboBox();
    }
    public void refreshTable() {
        List<Broker> data = brokerService.getAllBrokers();
        loadedList = data;
        ObservableList<Broker> observableData = FXCollections.observableArrayList(data);
        brokersTableView.setItems(observableData);

    }
    public void refreshComboBox() {
        countryComboBox.setItems(FXCollections.observableArrayList(countryService.getAllCountries()));

    }
    public void createBrokerAction(ActionEvent actionEvent) {
        if(nameTextField.getText().isEmpty() || profitMarginTextField.getText().isEmpty()
                || handlingFeeTextField.getText().isEmpty() || countryComboBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Fields cannot be empty!");
            alert.showAndWait();
        } else {
            brokerService.addBroker(nameTextField.getText(), Double.parseDouble(profitMarginTextField.getText()), Double.parseDouble(handlingFeeTextField.getText()), countryComboBox.getSelectionModel().getSelectedItem());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Broker has been added!");
            alert.showAndWait();
            refreshTable();
        }
    }

    public void updateCountrySelection(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        profitMargin.setCellValueFactory(new PropertyValueFactory<>("profitMargin"));
        handlingFee.setCellValueFactory(new PropertyValueFactory<>("handlingFee"));
        country.setCellValueFactory(new PropertyValueFactory<>("country"));

        List<Broker> data = new ArrayList<>();
        if(loadedList.size() == 0) {
            data = brokerService.getAllBrokers();
            loadedList = data;

        }  else {
            data = loadedList;
        }
        ObservableList<Broker> observableData = FXCollections.observableArrayList(data);
        brokersTableView.setItems(observableData);
        profitMarginTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,5}([\\.]\\d{0,8})?")) {
                profitMarginTextField.setText(oldValue);
            }
        });
        handlingFeeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,5}([\\.]\\d{0,8})?")) {
                handlingFeeTextField.setText(oldValue);
            }
        });
        refreshComboBox();

    }

    public void removeBrokerAction(ActionEvent actionEvent) {
        if(brokersTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You have to choose a broker to remove");
            alert.showAndWait();
            refreshTable();

        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Confirm");
            alert.setContentText("Do you want do remove " + brokersTableView.getSelectionModel().getSelectedItem().getName() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){

                brokerService.removeBroker(brokersTableView.getSelectionModel().getSelectedItem());

                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Confirmation");
                alert2.setHeaderText(null);
                alert2.setContentText("Broker has been removed!");
                alert2.showAndWait();
                refreshTable();
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
    }
}
