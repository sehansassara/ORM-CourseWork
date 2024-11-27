package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.RegistrationBo;
import lk.ijse.bo.custom.StudentBo;
import lk.ijse.dto.RegistrationDTO;
import lk.ijse.tm.RegistrationTm;

import java.io.IOException;
import java.util.List;

public class StudentDetailFormController {

    @FXML
    private AnchorPane anchorStudentDetail;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colCourse;

    @FXML
    private TableColumn<?, ?> colAdmin;

    @FXML
    private TableColumn<?, ?> colStudentEmail;

    @FXML
    private TableColumn<?, ?> colStudentIdthis;

    @FXML
    private TableColumn<?, ?> colStudentName;

    @FXML
    private TableColumn<?, ?> colStudentTel;

    @FXML
    private TableView<RegistrationTm> tblStudentCourse;

    private StudentBo studentBo = (StudentBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);
    private RegistrationBo registrationBo = (RegistrationBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.REGISTRATION);
    @FXML
    void tblStudentOnMouse(MouseEvent event) throws IOException {
    }

    @FXML
    public void initialize() {
        loadStudentCourses();
        setCellValueFactory();

    }

    private void setCellValueFactory() {
        colStudentIdthis.setCellValueFactory(new PropertyValueFactory<>("stuId"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("date"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("couId"));
    }

    private void loadStudentCourses() {
        ObservableList<RegistrationTm> obList = FXCollections.observableArrayList();
        List<RegistrationDTO> registrationDTOList = registrationBo.getAllStudentCourse();
        for (RegistrationDTO registrationDTO : registrationDTOList) {
            RegistrationTm registrationTm = new RegistrationTm(
                    registrationDTO.getId(),
                    registrationDTO.getDate(),
                    registrationDTO.getStudentId().getId(),
                    registrationDTO.getProgramId().getId(),
                    registrationDTO.getPaymentAmount()
            );
            obList.add(registrationTm);
        }
        tblStudentCourse.setItems(obList);
    }

}
