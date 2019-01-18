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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class AllStocksController implements Initializable {
    public static ArrayList<AllStocksTable> loadedList = new ArrayList<>();
    @Autowired
    UserService userService;
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
    protected void handleTransactionAction(ActionEvent event) throws IOException {
        System.out.println(userService);
        AllStocksTable table = allStockTableView.getSelectionModel().getSelectedItem();
        System.out.println(table.getShortName());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transaction.fxml"));
        loader.setControllerFactory(Application.app::getBean);
        Parent root = loader.load();

        TransactionController controller = loader.<TransactionController>getController();
        controller.setNameLabel(table.getShortName());
        controller.setUnitPrice(table.getPrice());
        Scene menu = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(menu);
            stage.show();
    }
    @FXML
    protected void refreshAction(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/window/menu.fxml"));
            loader.setControllerFactory(Application.app::getBean);
            Parent root = loader.load();
            Scene menu = new Scene(root);
            refreshTable();
            MenuController menuController = (MenuController) loader.getController();
            menuController.setSelectedTab(1);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(menu);
            stage.show();
        } catch (IOException e) {
            System.out.println("Refresh error");
            e.printStackTrace();
        }
    }
    public void refreshTable() {
        ArrayList<AllStocksTable> data = new ArrayList<>();
        data = StockDataService.getAllStocksTableList();
        loadedList = data;
        ObservableList<AllStocksTable> observableData = FXCollections.observableArrayList(data);
        allStockTableView.setItems(observableData);

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        allStockTableView.setItems(observableData);



    }



}