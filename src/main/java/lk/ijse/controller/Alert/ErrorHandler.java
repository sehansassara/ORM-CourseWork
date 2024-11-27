package lk.ijse.controller.Alert;

import javafx.scene.control.Alert;

public class ErrorHandler {
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
