package lk.ijse.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.UserBo;
import lk.ijse.controller.Alert.ErrorHandler;
import lk.ijse.controller.exception.ValidationException;
import lk.ijse.controller.util.Regex;
import lk.ijse.dto.UserDTO;
import lk.ijse.tm.UserTm;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserFormController {

    @FXML
    private AnchorPane anchorUser;

    @FXML
    private JFXComboBox<String> cmbUserRole;

    @FXML
    private TableColumn<?, ?> colUserEmail;

    @FXML
    private TableColumn<?, ?> colUserId;

    @FXML
    private TableColumn<?, ?> colUserName;

    @FXML
    private TableColumn<?, ?> colUserRole;

    @FXML
    private TableColumn<?, ?> colUserTel;

    @FXML
    private TextField txtUserEmail;

    @FXML
    private TextField txtUserId;

    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtUserPassword;

    @FXML
    private TextField txtUserTel;
    @FXML
    private TableView<UserTm> tblUser;

    UserBo userBo = (UserBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);


    public void initialize() throws SQLException, IOException, ClassNotFoundException {
        generateNewId();
        clearFields();
        setCellValueFactory();
        loadAllUser();

        ObservableList<String> roles = FXCollections.observableArrayList("Admin", "Admissions Coordinator");
        cmbUserRole.setItems(roles);
    }

    private void loadAllUser() {
        ObservableList<UserTm> obList = FXCollections.observableArrayList();
        List<UserDTO> userDTOList = null;
        try {
            userDTOList = userBo.getAllUser();
            for (UserDTO userDTO : userDTOList) {
                UserTm userTm = new UserTm(userDTO.getUser_id(),userDTO.getUser_name(),userDTO.getUser_email(),userDTO.getUser_tel(),userDTO.getPassword(),userDTO.getUser_role());
                obList.add(userTm);
            }
            tblUser.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        colUserEmail.setCellValueFactory(new PropertyValueFactory<>("user_email"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("user_name"));
        colUserRole.setCellValueFactory(new PropertyValueFactory<>("user_role"));
        colUserTel.setCellValueFactory(new PropertyValueFactory<>("user_tel"));
    }

    private void clearFields() {
        txtUserName.setText("");
        txtUserPassword.setText("");
        txtUserEmail.setText("");
        txtUserTel.setText("");

    }

    private String generateNewId() throws SQLException, IOException, ClassNotFoundException {
        String id = userBo.generateUserId();
        txtUserId.setText(id);
        return id;
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        try {
            String id = txtUserId.getText();
            String userName = txtUserName.getText();
            String userEmail = txtUserEmail.getText();
            String userTel = txtUserTel.getText();
            String password = BCrypt.hashpw(txtUserPassword.getText(), BCrypt.gensalt());
            String role = cmbUserRole.getValue();

            UserDTO userDTO = new UserDTO(id, userName, userEmail, userTel, password, role);

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this user?");
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        if (userBo.deleteUser(userDTO)) {
                            new Alert(Alert.AlertType.INFORMATION, "User deleted successfully!").show();
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Failed to delete user. User ID may not exist.").show();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage()).show();
        }

        clearFields();
        generateNewId();
        loadAllUser();

    }

   /* @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        if (!isValidInput()) {
            new Alert(Alert.AlertType.WARNING, "Please fill out all fields correctly.").show();
            return;
        }

        String id = txtUserId.getText();
        String userName = txtUserName.getText();
        String userEmail = txtUserEmail.getText();
        String userTel = txtUserTel.getText();
        String password = BCrypt.hashpw(txtUserPassword.getText(), BCrypt.gensalt());
        String role = cmbUserRole.getValue();

        UserDTO userDto = new UserDTO(id, userName, userEmail, userTel, password, role);
        if(userBo.save(userDto)){
            new Alert(Alert.AlertType.CONFIRMATION, "User Added Successfully!").show();

            generateNewId();
            clearFields();
            loadAllUser();

        } else {
            new Alert(Alert.AlertType.ERROR,"SQL Error").show();
        }
    }*/

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        try {
            if (!isValidInput()) {
                throw new ValidationException("Please fill out all fields correctly.");
            }

            String id = txtUserId.getText();
            String userName = txtUserName.getText();
            String userEmail = txtUserEmail.getText();
            String userTel = txtUserTel.getText();
            String password = BCrypt.hashpw(txtUserPassword.getText(), BCrypt.gensalt());
            String role = cmbUserRole.getValue();

            UserDTO userDto = new UserDTO(id, userName, userEmail, userTel, password, role);
            if (userBo.save(userDto)) {
                new Alert(Alert.AlertType.CONFIRMATION, "User Added Successfully!").show();
                generateNewId();
                clearFields();
                loadAllUser();
            } else {
                throw new SQLException("Failed to save user.");
            }

        } catch (ValidationException e) {
            ErrorHandler.showError("Validation Error", e.getMessage());
        } catch (SQLException | IOException e) {
            ErrorHandler.showError("System Error", "An unexpected error occurred: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/userUpdate_form.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("User Update");
        stage.show();
    }

    @FXML
    void cmbUserRoleOnAction(ActionEvent event) {
    }



    @FXML
    void tblUserOnMouse(MouseEvent event) {
        int index = tblUser.getSelectionModel().getSelectedIndex();

        if (index <= -1){
            return;
        }

        String id = colUserId.getCellData(index).toString();
        String name = colUserName.getCellData(index).toString();
        String email = colUserEmail.getCellData(index).toString();
        String tel = colUserTel.getCellData(index).toString();


        txtUserId.setText(id);
        txtUserName.setText(name);
        txtUserEmail.setText(email);
        txtUserTel.setText(tel);
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) throws IOException {
        try{
        String id = txtUserId.getText();

        UserDTO userDTO = null;
        userDTO = userBo.searchByCustomerId(id);

        if (userDTO != null) {
            txtUserId.setText(userDTO.getUser_id());
            txtUserName.setText(userDTO.getUser_name());
            txtUserEmail.setText(userDTO.getUser_email());
            txtUserTel.setText(userDTO.getUser_role());
        } else {
            new Alert(Alert.AlertType.INFORMATION, "User not found!").show();
        }
        } catch (NullPointerException e) {
            new Alert(Alert.AlertType.ERROR, "User not found!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "User not found!").show();
            e.printStackTrace();
        }
    }

    private boolean isValidInput() {
        if (txtUserId.getText().isEmpty() ||
                txtUserName.getText().isEmpty() ||
                txtUserEmail.getText().isEmpty() ||
                txtUserTel.getText().isEmpty() ||
                txtUserPassword.getText().isEmpty() ||
                cmbUserRole.getValue() == null) {
            return false;
        }


        if (!txtUserEmail.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            new Alert(Alert.AlertType.WARNING, "Invalid email format.").show();
            return false;
        }


        if (!txtUserTel.getText().matches("\\d{10}")) {
            new Alert(Alert.AlertType.WARNING, "Invalid phone number. Must be 10 digits.").show();
            return false;
        }


        if (txtUserPassword.getText().length() < 6) {
            new Alert(Alert.AlertType.WARNING, "Password must be at least 6 characters.").show();
            return false;
        }

        return true;
    }


    @FXML
    void txtUserEmailOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.util.TextField.EMAIL,txtUserEmail);
    }

    @FXML
    void txtUserNameOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.util.TextField.NAME,txtUserName);
    }

    @FXML
    void txtUserTelOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.util.TextField.CONTACT,txtUserTel);
    }


}
