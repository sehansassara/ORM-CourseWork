package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.UserBo;
import lk.ijse.controller.util.Regex;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class ForgetPassword3FormController {

    @FXML
    private AnchorPane anchorpainForget3;

    @FXML
    private JFXButton btnSave;

    @FXML
    private ImageView imageEye;

    @FXML
    private ImageView imageEye1;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private TextField txtConfirmPassword1;

    @FXML
    private PasswordField txtNewPassword;

    @FXML
    private TextField txtNewPassword1;

    UserBo userBo = (UserBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane loginPane = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));

        Scene scene = new Scene(loginPane);

        Stage stage = (Stage) anchorpainForget3.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws IOException {
        String newPassword = txtNewPassword.getText();
        String conPassword = txtConfirmPassword.getText();
        String userName = SendOtpFormController.userName;

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        if (newPassword.equalsIgnoreCase(conPassword)) {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            if (userBo.updatePass(hashedPassword, userName)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Password Updated!!").show();

                AnchorPane loginPane = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));
                Scene scene = new Scene(loginPane);
                Stage stage = (Stage) anchorpainForget3.getScene().getWindow();
                stage.setScene(scene);
                stage.centerOnScreen();
            }
            txtConfirmPassword.setStyle("-fx-border-color: green;");
            txtNewPassword.setStyle("-fx-border-color: green;");
        } else {
            txtConfirmPassword.setStyle("-fx-border-color: #e74c3c;");
            txtNewPassword.setStyle("-fx-border-color: #e74c3c;");
            new Alert(Alert.AlertType.INFORMATION, "Passwords do not match!!").show();
        }
    }


    @FXML
    void hidePasswordOnMouseReleased(MouseEvent event) {
        txtNewPassword.setVisible(true);
        txtNewPassword1.setVisible(false);
    }
    @FXML
    void seePasswordOnMousePressed(MouseEvent event) {
        txtNewPassword.setVisible(false);
        txtNewPassword1.setText(txtNewPassword.getText());
        txtNewPassword1.setVisible(true);
    }


    @FXML
    void seePasswordConOnMousePressed(MouseEvent event) {
        txtConfirmPassword.setVisible(false);
        txtConfirmPassword1.setText(txtConfirmPassword.getText());
        txtConfirmPassword1.setVisible(true);
    }

    @FXML
    void hidePasswordConOnMouseReleased(MouseEvent event) {
        txtConfirmPassword.setVisible(true);
        txtConfirmPassword1.setVisible(false);
    }

    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.controller.util.TextField.ADDRESS,txtNewPassword)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.util.TextField.ADDRESS,txtConfirmPassword)) return false;
        return true;
    }

    @FXML
    void txtConKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.util.TextField.ADDRESS,txtConfirmPassword);
    }

    @FXML
    void txtNewKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.util.TextField.ADDRESS,txtNewPassword);
    }


}
