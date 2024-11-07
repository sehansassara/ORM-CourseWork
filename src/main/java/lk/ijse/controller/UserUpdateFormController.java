package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.UserBo;
import lk.ijse.dto.UserDTO;
import org.mindrot.jbcrypt.BCrypt;

public class UserUpdateFormController {

    @FXML
    private TextField txtCurrentPassword;

    @FXML
    private TextField txtNewPassword;

    @FXML
    private TextField txtNewUserName;

    @FXML
    private TextField txtUserId;

    private UserBo userBo = (UserBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);

    @FXML
    void btnUpdateUserOnAction(ActionEvent event) {
        try {
            String userId = txtUserId.getText();
            String currentPassword = txtCurrentPassword.getText();
            String newPassword = txtNewPassword.getText();
            String newUserName = txtNewUserName.getText();

            if (userId.isEmpty() || currentPassword.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "User ID and Current Password are required.").show();
                return;
            }

            UserDTO userDto = userBo.getUserByName(userId);
            if (userDto == null) {
                new Alert(Alert.AlertType.ERROR, "User not found.").show();
                return;
            }

            if (!BCrypt.checkpw(currentPassword, userDto.getPassword())) {
                new Alert(Alert.AlertType.ERROR, "Incorrect current password.").show();
                return;
            }

            boolean isUpdated = false;

            if (!newUserName.isEmpty() && !newUserName.equals(userDto.getUser_name())) {
                userDto.setUser_name(newUserName);
                isUpdated = true;
            }

            if (!newPassword.isEmpty()) {
                String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                userDto.setPassword(hashedPassword);
                isUpdated = true;
            }

            if (isUpdated) {
                if (userBo.update(userDto)) {
                    new Alert(Alert.AlertType.CONFIRMATION, "User updated successfully!").show();
                    Parent root = FXMLLoader.load(getClass().getResource("/view/dashBoard_form.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.setTitle("Dashboard");
                    stage.show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to update user.").show();
                }
            } else {
                new Alert(Alert.AlertType.INFORMATION, "No changes detected.").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage()).show();
        }
    }
}
