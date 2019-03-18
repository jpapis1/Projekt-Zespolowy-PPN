package app.other;

import javafx.scene.control.Alert;

public class Messenger {
    public static void infoBox(String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(info);
        alert.showAndWait();

    }
    public static void errorBox(String error) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();
    }
}
