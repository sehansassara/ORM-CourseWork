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

public class SigninFormController {

    @FXML
    private AnchorPane login1;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtTel;

    @FXML
    private TextField txtUserId;

    @FXML
    private ComboBox<String> cmbSelectRolle;

    UserBo userBo = (UserBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);


    public void initialize() throws SQLException, IOException, ClassNotFoundException {
        generateNewId();
        clearFields();

        ObservableList<String> roles = FXCollections.observableArrayList("Admin", "Admissions Coordinator");
        cmbSelectRolle.setItems(roles);
    }

    private void clearFields() {
        txtName.setText("");
        txtPassword.setText("");
        txtEmail.setText("");
        txtTel.setText("");

    }

    private String generateNewId() throws SQLException, IOException, ClassNotFoundException {
        String id = userBo.generateUserId();
        txtUserId.setText(id);
        return id;
    }

    @FXML
    void btnSignInOnAction(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        if (!isValidInput()) {
            new Alert(Alert.AlertType.WARNING, "Please fill out all fields correctly.").show();
            return;
        }

        String id = txtUserId.getText();
        String userName = txtName.getText();
        String userEmail = txtEmail.getText();
        String userTel = txtTel.getText();
        String password = BCrypt.hashpw(txtPassword.getText(), BCrypt.gensalt());
        String role = cmbSelectRolle.getValue();

        UserDTO userDto = new UserDTO(id, userName, userEmail, userTel, password, role);
        if(userBo.save(userDto)){
            new Alert(Alert.AlertType.CONFIRMATION, "User Added Successfully!").show();

            generateNewId();
            clearFields();

            Parent root = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login form");
            stage.show();
        } else {
            new Alert(Alert.AlertType.ERROR,"SQL Error").show();
        }
    }

    private boolean isValidInput() {
        if (txtUserId.getText().isEmpty() ||
                txtName.getText().isEmpty() ||
                txtEmail.getText().isEmpty() ||
                txtTel.getText().isEmpty() ||
                txtPassword.getText().isEmpty() ||
                cmbSelectRolle.getValue() == null) {
            return false;
        }


        if (!txtEmail.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            new Alert(Alert.AlertType.WARNING, "Invalid email format.").show();
            return false;
        }


        if (!txtTel.getText().matches("\\d{10}")) {
            new Alert(Alert.AlertType.WARNING, "Invalid phone number. Must be 10 digits.").show();
            return false;
        }


        if (txtPassword.getText().length() < 6) {
            new Alert(Alert.AlertType.WARNING, "Password must be at least 6 characters.").show();
            return false;
        }

        return true;
    }

    @FXML
    void cmbSelectRolleOnAction(ActionEvent event) {
        String selectedRole = cmbSelectRolle.getSelectionModel().getSelectedItem();
        System.out.println("Selected Role: " + selectedRole);
    }

}
