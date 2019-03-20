package app.view.controller;

import app.model.Broker;
import app.model.Permission;
import app.model.User;
import app.other.Messenger;
import app.service.BrokerService;
import app.service.PermissionService;
import app.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Controller
public class UsersController implements Initializable {
    public static List<User> loadedList = new ArrayList<>();


    @FXML
    public TableColumn<User,String> username;
    @FXML
    public TableColumn<User,String> email;
    @FXML
    public TableColumn<User,String> firstName;
    @FXML
    public TableColumn<User,String> lastName;
    @FXML
    public TableColumn<User,Double> funds;
    @FXML
    public TableColumn<User,Permission> permission;
    @FXML
    public TableColumn<User,Broker> broker;

    @FXML
    public TableView<User> usersTableView;


    @FXML
    public ComboBox<Permission> permissionComboBox;
    @FXML
    public ComboBox<Broker> brokerComboBox;

    @FXML
    public TextField usernameTextField;
    @FXML
    public TextField passwordTextField;
    @FXML
    public TextField emailTextField;
    @FXML
    public TextField fundsTextField;
    @FXML
    public TextField firstNameTextField;
    @FXML
    public TextField lastNameTextField;

    @Autowired
    UserService userService;
    @Autowired
    PermissionService permissionService;
    @Autowired
    BrokerService brokerService;

    public void refreshAction(ActionEvent actionEvent) {
        refreshTable();
        refreshComboBoxes();
    }

    public void refreshTable() {
        List<User> data = userService.getAllUsers();
        loadedList = data;
        ObservableList<User> observableData = FXCollections.observableArrayList(data);
        usersTableView.setItems(observableData);

    }
    public void refreshComboBoxes() {
        permissionComboBox.setItems(FXCollections.observableArrayList(permissionService.getAllPermissions()));
        brokerComboBox.setItems(FXCollections.observableArrayList(brokerService.getAllBrokers()));


    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        funds.setCellValueFactory(new PropertyValueFactory<>("funds"));
        permission.setCellValueFactory(new PropertyValueFactory<>("permission"));
        broker.setCellValueFactory(new PropertyValueFactory<>("broker"));

        fundsTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,8}([\\.]\\d{0,2})?")) {
                fundsTextField.setText(oldValue);
            }
        });
        List<User> data = new ArrayList<>();
        if(loadedList.size() == 0) {
            data = userService.getAllUsers();
            loadedList = data;

        }  else {
            data = loadedList;
        }
        ObservableList<User> observableData = FXCollections.observableArrayList(data);
        usersTableView.setItems(observableData);
        refreshComboBoxes();
    }

    public void createUserAction(ActionEvent actionEvent) {
        if(usernameTextField.getText().isEmpty() || emailTextField.getText().isEmpty() ||
                fundsTextField.getText().isEmpty() || firstNameTextField.getText().isEmpty() ||
                lastNameTextField.getText().isEmpty() || brokerComboBox.getSelectionModel().isEmpty() ||
                permissionComboBox.getSelectionModel().isEmpty()) {
                Messenger.errorBox("Fields cannot be empty!");
        } else {
            try {
                userService.createUser(new User.UserBuilder(usernameTextField.getText())
                        .fullName(firstNameTextField.getText(), lastNameTextField.getText())
                        .mail(emailTextField.getText())
                        .funds(Double.parseDouble(fundsTextField.getText()))
                        .broker(brokerComboBox.getSelectionModel().getSelectedItem())
                        .pass(userService.hashPassword(passwordTextField.getText()))
                        .perm(permissionComboBox.getSelectionModel().getSelectedItem()).build());

                Messenger.infoBox("User has been added");
                refreshTable();
            } catch (DataAccessException e) {

                Messenger.errorBox("Data violation! - duplicate username or email");
            }

        }
    }

    public void removeUserAction(ActionEvent actionEvent) {

        if(usersTableView.getSelectionModel().isEmpty()) {
            Messenger.errorBox("You have to choose a broker to remove");
            refreshTable();

        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Confirm");
            alert.setContentText("Do you want do remove " + usersTableView.getSelectionModel().getSelectedItem().getUsername() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                try {
                    userService.removeUser(usersTableView.getSelectionModel().getSelectedItem());
                    Messenger.infoBox("User has been removed!");
                    refreshTable();
                } catch (DataAccessException e) {
                    Messenger.errorBox("Data violation!");
                }
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
    }
}
