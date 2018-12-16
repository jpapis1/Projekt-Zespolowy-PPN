package app.view.controller;

import app.api.StockDataService;
import app.view.table.AllStocksTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class AllStocksController implements Initializable {

    @FXML
    private TableView<AllStocksTable> allStockTableView;
    @FXML
    private TableColumn<AllStocksTable, String> sector;
    @FXML
    private TableColumn<AllStocksTable, String> shortName;
    @FXML
    private TableColumn<AllStocksTable, String> name;
    @FXML
    private TableColumn<AllStocksTable, Date> date;
    @FXML
    private TableColumn<AllStocksTable, Double> price;
    @FXML
    private TableColumn<AllStocksTable, Button> action;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        sector.setCellValueFactory(new PropertyValueFactory<>("sector"));
        shortName.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        action.setCellValueFactory(new PropertyValueFactory<>("action"));
        ArrayList<AllStocksTable> data = StockDataService.getAllStocksTableList();
        ObservableList<AllStocksTable> observableData = FXCollections.observableArrayList(data);
        allStockTableView.setItems(observableData);

    }


}