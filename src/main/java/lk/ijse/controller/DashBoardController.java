package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DashBoardController {

    @FXML
    private AnchorPane anchorDash1;


    @FXML
    private Label lblStudentCount;

    @FXML
    void btnDashBoardOnAction(ActionEvent event) {

    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) {

    }

    @FXML
    void btnPaymentOnAction(ActionEvent event) {

    }

    @FXML
    void btnProgramOnAction(ActionEvent event) throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/program_form.fxml"));

        anchorDash1.getChildren().clear();
        anchorDash1.getChildren().add(dashboardPane);
    }

    @FXML
    void btnStudentOnAction(ActionEvent event) throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/student_form.fxml"));

        anchorDash1.getChildren().clear();
        anchorDash1.getChildren().add(dashboardPane);
    }

    @FXML
    void btnUserOnAction(ActionEvent event) throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/user_form.fxml"));

        anchorDash1.getChildren().clear();
        anchorDash1.getChildren().add(dashboardPane);
    }

}
