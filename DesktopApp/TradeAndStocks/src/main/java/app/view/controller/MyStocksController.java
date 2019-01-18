package app.view.controller;

import app.Application;
import app.api.StockDataService;
import app.service.UserService;
import app.view.table.AllStocksTable;
import app.view.table.MyStocksTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

@Controller
public class MyStocksController implements Initializable {
    public static ArrayList<MyStocksTable> loadedList = new ArrayList<>();
    @Autowired
    UserService userService;
    @FXML
    private TableView<MyStocksTable> myStocksTableView;
    @FXML
    private TableColumn<MyStocksTable, String> shortName;
    @FXML
    private TableColumn<MyStocksTable, Double> unitPrice;
    @FXML
    private TableColumn<MyStocksTable, Double> price;
    @FXML
    private TableColumn<MyStocksTable, Double> units;
    @FXML
    private TableColumn<MyStocksTable, Double> profitLoss;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        shortName.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        unitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        price.setCellValueFactory(new PropertyValueFactory<>("value"));
        units.setCellValueFactory(new PropertyValueFactory<>("units"));
        profitLoss.setCellValueFactory(new PropertyValueFactory<>("profitLoss"));

        ArrayList<MyStocksTable> data = new ArrayList<>();
        System.out.println("LOADING");
        if(loadedList.size() == 0) {
            data = StockDataService.getMyStocksTableList(userService);
            loadedList = data;

        }  else {
            data = loadedList;
        }
        ObservableList<MyStocksTable> observableData = FXCollections.observableArrayList(data);
        myStocksTableView.setItems(observableData);
    }
    public void refreshTable() {
        ArrayList<MyStocksTable> data = new ArrayList<>();
        data = StockDataService.getMyStocksTableList(userService);
        loadedList = data;
        ObservableList<MyStocksTable> observableData = FXCollections.observableArrayList(data);
        myStocksTableView.setItems(observableData);

    }

    public void refreshAction(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/window/menu.fxml"));
            loader.setControllerFactory(Application.app::getBean);

            Parent root = loader.load();
            Scene menu = new Scene(root);
            refreshTable();
            MenuController menuController = (MenuController) loader.getController();
            menuController.setSelectedTab(2);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(menu);
            stage.show();
        } catch (IOException e) {
            System.out.println("Refresh error");
            e.printStackTrace();
        }
    }

    public void stockInfoAction(ActionEvent actionEvent) {
    }
}
