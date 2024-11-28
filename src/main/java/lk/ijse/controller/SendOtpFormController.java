package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.UserBo;
import lk.ijse.dto.UserDTO;
import lk.ijse.entity.User;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Random;

public class SendOtpFormController {

    @FXML
    private AnchorPane AnchorpaneForget;

    @FXML
    private JFXButton btnSendOtp;

    @FXML
    private Label lblEmail;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtUserName;

    public static int OTP;
    public static String userName;

    UserBo userBo = (UserBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);

    @FXML
    void btnSendOtpOnAction(ActionEvent event) throws IOException, MessagingException {
        int otp = new Random().nextInt(9000) + 1000;
        JavaMail.sendMail(txtEmail.getText(),otp);
        OTP=otp;
        System.out.println(">>>"+otp);

        Parent root = FXMLLoader.load(getClass().getResource("/view/forgetPassword2_form.fxml"));
        Scene scene = btnSendOtp.getScene();
        root.translateXProperty().set(scene.getWidth());

        AnchorPane parentContainer = (AnchorPane) scene.getRoot();
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1),kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event1 -> {
            parentContainer.getChildren().remove(AnchorpaneForget);
        });
        timeline.play();
    }

    @FXML
    void txtUserNameOnAction(ActionEvent event) {
        userName = txtUserName.getText();
        UserDTO user = userBo.getUserByName(userName);


        if (user != null) {
            if (user.getUser_email() != null) {
                txtEmail.setText(user.getUser_email());

            }else{
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Information");
                infoAlert.setHeaderText(null);
                infoAlert.setContentText("Please Enter Email Address .");
                infoAlert.showAndWait();
            }


        } else {
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Information");
            infoAlert.setHeaderText(null);
            infoAlert.setContentText("User not found .");
            txtEmail.setText(null);
            infoAlert.showAndWait();
        }
    }

}
