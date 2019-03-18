package app.view.controller;

import app.model.Country;
import app.other.Messenger;
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
public class CountriesController implements Initializable {
    public static List<Country> loadedList = new ArrayList<>();
    @FXML
    public TableColumn name;
    @FXML
    public TableColumn taxRate;
    @FXML
    public TableView<Country> countriesTableView;
    @FXML
    public TextField nameTextField;
    @FXML
    public TextField taxRateTextField;
    @Autowired
    CountryService countryService;

    public void refreshAction(ActionEvent actionEvent) {
        refreshTable();
    }

    public void createCountryAction(ActionEvent actionEvent) {
        if(nameTextField.getText().isEmpty() || taxRateTextField.getText().isEmpty()) {
            Messenger.errorBox("Field cannot be empty!");

        } else {
            countryService.addCountry(nameTextField.getText(), Double.parseDouble(taxRateTextField.getText()));
            Messenger.infoBox("Country has been added");
            refreshTable();
        }
    }
    public void refreshTable() {
        List<Country> data = countryService.getAllCountries();
        loadedList = data;
        ObservableList<Country> observableData = FXCollections.observableArrayList(data);
        countriesTableView.setItems(observableData);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        taxRate.setCellValueFactory(new PropertyValueFactory<>("taxRate"));

        List<Country> data = new ArrayList<>();
        System.out.println("LOADING");
        if(loadedList.size() == 0) {
            data = countryService.getAllCountries();
            loadedList = data;

        }  else {
            data = loadedList;
        }
        ObservableList<Country> observableData = FXCollections.observableArrayList(data);
        countriesTableView.setItems(observableData);
        taxRateTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,5}([\\.]\\d{0,8})?")) {
                taxRateTextField.setText(oldValue);
            }
        });
    }

    public void removeCountryAction(ActionEvent actionEvent) {
        if(countriesTableView.getSelectionModel().isEmpty()) {
            Messenger.errorBox("You have to choose a country to remove!");
            refreshTable();

        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Confirm");
            alert.setContentText("Do you want do remove " + countriesTableView.getSelectionModel().getSelectedItem().getName() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){

                countryService.removeCountry(countriesTableView.getSelectionModel().getSelectedItem());
                Messenger.infoBox("Country has been removed!");
                refreshTable();
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
    }
}
