package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFormController {
    @FXML
    private JFXButton btnDash;

    @FXML
    private JFXButton btnPay;

    @FXML
    private JFXButton btnPro;

    @FXML
    private JFXButton btnReg;

    @FXML
    private JFXButton btnStu;

    @FXML
    private JFXButton btnUser;

    @FXML
    private JFXButton btnSetting;

    @FXML
    private AnchorPane anchorAll;

    @FXML
    private AnchorPane anchorMain;

    public void initialize() throws IOException {
        loadDashboardForm();
    }

    private void loadDashboardForm() throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/dashBorad_form.fxml"));


        anchorMain.getChildren().clear();
        anchorMain.getChildren().add(dashboardPane);
    }

    @FXML
    void btnDashBoardOnAction(ActionEvent event) throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/dashBorad_form.fxml"));

        anchorMain.getChildren().clear();
        anchorMain.getChildren().add(dashboardPane);
    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) throws IOException {
        AnchorPane loginPane = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));

        Scene scene = new Scene(loginPane);

        Stage stage = (Stage) anchorAll.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    void btnPaymentOnAction(ActionEvent event) throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/payment_form.fxml"));

        anchorMain.getChildren().clear();
        anchorMain.getChildren().add(dashboardPane);
    }

    @FXML
    void btnProgramOnAction(ActionEvent event) throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/program_form.fxml"));

        anchorMain.getChildren().clear();
        anchorMain.getChildren().add(dashboardPane);
    }

    @FXML
    void btnRegistrationOnAction(ActionEvent event) throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/registration_form.fxml"));

        anchorMain.getChildren().clear();
        anchorMain.getChildren().add(dashboardPane);
    }

    @FXML
    void btnStudentOnAction(ActionEvent event) throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/student_form.fxml"));

        anchorMain.getChildren().clear();
        anchorMain.getChildren().add(dashboardPane);
    }

    @FXML
    void btnUserOnAction(ActionEvent event) throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/user_form.fxml"));

        anchorMain.getChildren().clear();
        anchorMain.getChildren().add(dashboardPane);
    }


    public void enableAdminFeatures() {
        btnDash.setDisable(false);
        btnPay.setDisable(false);
        btnPro.setDisable(false);
        btnReg.setDisable(false);
        btnStu.setDisable(false);
        btnUser.setDisable(false);
        btnSetting.setDisable(false);
    }

    public void disableAdministratorButtons() {
        btnDash.setDisable(false);
        btnPay.setDisable(false);
        btnPro.setDisable(false);
        btnReg.setDisable(false);
        btnStu.setDisable(false);
        btnUser.setDisable(true);
        btnSetting.setDisable(false);
    }

    @FXML
    void btnSettingOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/userUpdate_form.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("User Update");
        stage.show();
    }
}
