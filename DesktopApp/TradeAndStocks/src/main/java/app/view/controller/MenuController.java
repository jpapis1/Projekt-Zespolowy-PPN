package app.view.controller;

import app.model.PermissionEnum;
import app.model.User;
import app.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class MenuController implements Initializable {
    public static Label infoLabelP;
    @FXML
    private Label infoLabel;

    @FXML
    private VBox myStocksTab;

    @FXML
    private TabPane tabPanel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = UserService.getActiveUser();
        if(user.getPermission().getName()== PermissionEnum.client) {
            infoLabel.setText("Current balance: $" + String.valueOf(user.getFunds()) +
                    " | Broker: " + user.getBroker() + " | Handling Fee: " + user.getBroker().getHandlingFee() * 100 + "%" +
                    " | Profit Margin: " + user.getBroker().getProfitMargin() + "%" + " | Tax rate: " + user.getBroker().getCountry().getTaxRate() * 100 + "%");
            infoLabelP = infoLabel;
        } else if(user.getPermission().getName() == PermissionEnum.admin) {
            infoLabel.setText("Admin mode");
        }
    }
    public void setSelectedTab(int index) {
        SingleSelectionModel<Tab> selectionModel = tabPanel.getSelectionModel();
        selectionModel.select(index);
    }


}
