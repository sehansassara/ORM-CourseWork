package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.UserBo;
import lk.ijse.dto.UserDTO;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;

public class LoginFormController {

    @FXML
    private AnchorPane login1;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserName;

    @FXML
    private ComboBox<String> cmbSelectRolle;


    UserBo userBo = (UserBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);


    public void initialize() {
        ObservableList<String> roles = FXCollections.observableArrayList("Admin", "Admissions Coordinator");
        cmbSelectRolle.setItems(roles);
    }


    @FXML
    void btnLoginOnAction(ActionEvent event) {
        try {
            String userName = txtUserName.getText();
            String password = txtPassword.getText();

            if (userName.isEmpty() || password.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Username and password are required.").show();
                return;
            }

            UserDTO userDto = userBo.getUserByName(userName);
            if (userDto != null) {
                if (BCrypt.checkpw(password, userDto.getPassword())) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Login successful!").show();

                    Parent root = FXMLLoader.load(getClass().getResource("/view/dashBoard_form.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.setTitle("Dashboard");
                    stage.show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Invalid password.").show();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "User not found.").show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnSignInOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/signin_form.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("SignIn Form");
        stage.show();
    }

    @FXML
    void cmbSelectRolleOnAction(ActionEvent event) {

    }

}
