package app.view.controller;

import app.Application;
import app.api.StockDataService;
import app.service.TransactionService;
import app.service.UserService;
import app.view.table.AllStocksTable;
import app.view.table.MyStocksTable;
import app.view.table.StockInfoTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

@Controller
public class MyStocksController implements Initializable {
    public static ArrayList<MyStocksTable> loadedList = new ArrayList<>();
    @Autowired
    UserService userService;
    @Autowired
    TransactionService transactionService;
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/window/menu.fxml"));
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
        MyStocksTable table = myStocksTableView.getSelectionModel().getSelectedItem();
        Double currentUnitPrice = table.getUnitPrice();
        Double allUnitsOwned = table.getUnits();
        Double currentSum = table.getValue();

        TableView<StockInfoTable> stockInfoTableTableView = new TableView<>();
        TableColumn<StockInfoTable,Double> pUnitPrice = new TableColumn<>();
        TableColumn<StockInfoTable,Double> sUnits = new TableColumn<>();
        TableColumn<StockInfoTable,Double> pValue = new TableColumn<>();
        TableColumn<StockInfoTable,Double> cValue = new TableColumn<>();
        TableColumn<StockInfoTable,Date> dateOfTransaction = new TableColumn<>();
        TableColumn<StockInfoTable,String> profitMargin = new TableColumn<>();
        pUnitPrice.setText("Price at the day of transaction");
        sUnits.setText("Units");
        pValue.setText("Value");
        cValue.setText("Current value");
        profitMargin.setText("Profit margin");
        dateOfTransaction.setText("Date of transaction");
        pUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPriceAtTheDayOfPurchase"));
        sUnits.setCellValueFactory(new PropertyValueFactory<>("units"));
        pValue.setCellValueFactory(new PropertyValueFactory<>("valueAtTheDayOfPurchase"));
        cValue.setCellValueFactory(new PropertyValueFactory<>("currentValue"));
        dateOfTransaction.setCellValueFactory(new PropertyValueFactory<>("dateOfTransaction"));
        profitMargin.setCellValueFactory(new PropertyValueFactory<>("profitLoss"));
        stockInfoTableTableView.getColumns().addAll(pUnitPrice,sUnits,pValue,cValue,dateOfTransaction,profitMargin);

        ArrayList<StockInfoTable> data = new ArrayList<>();
        data = transactionService.populateStockInfoTable(UserService.getActiveUser(),table.getShortName(),currentUnitPrice);
        //data.add(new StockInfoTable(203.12,2.3,203.12*2.3,203.12*currentUnitPrice,new Date(),true));
        //data.add(new StockInfoTable(203.12,2.3,203.12*2.3,203.12*currentUnitPrice,new Date(),false));
        ObservableList<StockInfoTable> oData = FXCollections.observableArrayList(data);
        stockInfoTableTableView.setItems(oData);





        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        Label nameOfStockLabel = new Label("Stock: " + table.getShortName());
        Label currentUnitPriceLabel = new Label("Current unit price: " + currentUnitPrice);
        Label allUnitsOwnedLabel = new Label("Sum of all units owned: " + allUnitsOwned);
        Label currentValueOfStockLabel = new Label("Current value of stocks owned: " + currentSum);

        Separator separator = new Separator();


        root.getChildren().addAll(nameOfStockLabel,currentUnitPriceLabel,allUnitsOwnedLabel,currentValueOfStockLabel,separator,stockInfoTableTableView);

        Scene scene = new Scene(root, 850, 400);
        Stage stage = new Stage();
        stage.setTitle("JavaFX Error Alert (o7planning.org)");
        stage.setScene(scene);

        stage.show();
    }
}
