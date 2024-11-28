package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.ProgramBo;
import lk.ijse.bo.custom.RegistrationBo;
import lk.ijse.bo.custom.StudentBo;
import lk.ijse.dto.ProgramDTO;
import lk.ijse.dto.RegistrationDTO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.entity.Program;
import lk.ijse.entity.Student;
import lk.ijse.tm.RegistrationTm;
import lk.ijse.tm.StudentTm;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegistrationFormController {

    @FXML
    private AnchorPane anchorRegister;

    @FXML
    private JFXComboBox<String> cmbPrograms;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colStuId;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblProgramDuration;

    @FXML
    private Label lblProgramFee;

    @FXML
    private Label lblProgramID;

    @FXML
    private Label lblRegId;

    @FXML
    private Label lblStudentName;

    @FXML
    private Label lblTotalAmount;

    @FXML
    private TableView<StudentTm> tblCart;

    @FXML
    private TextField txtStudentId;

    @FXML
    private TableColumn<?, ?> colPrograms;

    private ObservableList<RegistrationTm> obList = FXCollections.observableArrayList();

    StudentBo studentBo = (StudentBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);

    ProgramBo programBo = (ProgramBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PROGRAM);
    RegistrationBo registrationBo = (RegistrationBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.REGISTRATION);


    public void initialize() throws SQLException, IOException, ClassNotFoundException {
        generateRegId();
        setDate();
        getProgramsNames();
        loadStudentsRegisteredForAllCulinaryPrograms();
        setCellValueFactory();
        clearfields();
    }

    private void clearfields() {
        lblProgramID.setText("");
        lblProgramFee.setText("");
        lblProgramDuration.setText("");
    }

    private void loadStudentsRegisteredForAllCulinaryPrograms() throws IOException {
        ObservableList<StudentTm> obList = FXCollections.observableArrayList();
        List<StudentDTO> studentList = studentBo.getStudentsRegisteredForAllCulinaryPrograms();
        System.out.println("Number of students retrieved: " + studentList.size());
        for (StudentDTO studentDTO : studentList) {
            String userId = studentDTO.getUser() != null ? studentDTO.getUser().getUser_id() : null;
            String proId = null;
            if (studentDTO.getRegistration() != null &&
                    studentDTO.getRegistration().getProgram() != null &&
                    !studentDTO.getRegistration().getProgram().getRegistrations().isEmpty()) {
                proId = studentDTO.getRegistration().getProgram().getId(); // Get the first program ID
            }
            StudentTm studentTm = new StudentTm(
                    studentDTO.getId(),
                    userId,
                    studentDTO.getName(),
                    studentDTO.getPhoneNumber(),
                    studentDTO.getEmail(),
                    studentDTO.getAddress(),
                    proId

            );
            obList.add(studentTm);
        }
        tblCart.setItems(obList);
    }

    private void setCellValueFactory() {
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
      colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStuId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void getProgramsNames() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        List<String> batIdList = registrationBo.getIds();

        for (String name : batIdList) {
            obList.add(name);
        }
        cmbPrograms.setItems(obList);

    }

    public void setStudentData(StudentDTO student) {
        txtStudentId.setText(student.getId());
        lblStudentName.setText(student.getName());

    }

    private void setDate() {
        LocalDate currentDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = currentDate.format(formatter);

        lblDate.setText(formattedDate);
    }

    private String generateRegId() {
        String id = registrationBo.generateRegId();
        lblRegId.setText(id);
        return id;
    }

   /* @FXML
    void btnAddOnAction(ActionEvent event) {
        String regId = lblRegId.getText();
        String stuId = txtStudentId.getText();
        String proId = lblProgramID.getText();
        double fee = Double.parseDouble(lblProgramFee.getText());
        String date = (lblDate.getText());

        JFXButton btnRemove = new JFXButton("remove");
        btnRemove.setCursor(Cursor.HAND);

        btnRemove.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> types = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if(types.orElse(no) == yes) {
                int selectedIndex = tblCart.getSelectionModel().getSelectedIndex();
                obList.remove(selectedIndex);

                tblCart.refresh();

                lblTotalAmount.setText(String.valueOf(calculateTotalFee()));
            }
        });

        RegistrationTm tm = new RegistrationTm(regId, date, stuId, proId, fee, btnRemove);
        obList.add(tm);

        tblCart.setItems(obList);

        lblTotalAmount.setText(String.valueOf(calculateTotalFee()));

    }*/

 /*   private double calculateTotalFee() {
        double total = 0.0;

        for (RegistrationTm registrationTm : obList) {
            total += registrationTm.getFee();
        }

        return total;
    }*/

    @FXML
    void btnSaveOnAction(ActionEvent event) throws IOException {
        String regId = lblRegId.getText();
        String stuId = txtStudentId.getText();
        String proId = lblProgramID.getText();
        String regDate = lblDate.getText();
        double amount = Double.parseDouble(lblProgramFee.getText());

        Student student = registrationBo.getStudentById(stuId);
        Program program = registrationBo.getProgramDetailsById(proId);

        boolean isAlreadyRegistered = registrationBo.isStudentRegisteredForProgram(stuId, proId);

        if (isAlreadyRegistered) {
            new Alert(Alert.AlertType.WARNING, "Student is already registered for this program!").show();
            return;
        }

        RegistrationDTO registrationDTO = new RegistrationDTO(regId, program, student, regDate, amount);

        try {
            if (registrationBo.saveregisterdStudent(registrationDTO)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Added Course for student!").show();
                openPaymentForm(stuId, regId,amount);
                clearfields();
            } else {
                new Alert(Alert.AlertType.ERROR, "SQL Error").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void btnPaymentOnAction(ActionEvent event) {
        String stuId = txtStudentId.getText();
        String regId = lblRegId.getText();
        double amount = Double.parseDouble(lblProgramFee.getText());
        openPaymentForm(stuId,regId,amount);
    }
    private void openPaymentForm(String stuid,String regId, double amount) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/payment_form.fxml"));
            AnchorPane registrationForm = loader.load();
            PaymentFormController paymentFormController = loader.getController();
            paymentFormController.setRegistrationData(stuid,regId,amount);
            anchorRegister.getChildren().clear();
            anchorRegister.getChildren().add(registrationForm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnViewOnAction(ActionEvent event) throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/studentDetail_form.fxml"));

        anchorRegister.getChildren().clear();
        anchorRegister.getChildren().add(dashboardPane);
    }

    @FXML
    void cmbProgramOnAction(ActionEvent event) {
        String selectedProgramId = cmbPrograms.getValue(); // Get selected program ID

        try {
            ProgramDTO programDTO = programBo.getProgramDetailsByName(selectedProgramId);

            lblProgramID.setText(programDTO.getId());
            lblProgramDuration.setText(programDTO.getDuration());
            lblProgramFee.setText(String.valueOf(programDTO.getFee()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void tblUserOnMouse(MouseEvent event) {

    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        try {
            String id = txtStudentId.getText();
            StudentDTO studentDTO = registrationBo.searchByStudentId(id);

            if (studentDTO != null) {
                txtStudentId.setText(studentDTO.getId());
                lblStudentName.setText(studentDTO.getName());

            } else {
                new Alert(Alert.AlertType.INFORMATION, "Program not found!").show();
            }
        } catch (NullPointerException e) {
            new Alert(Alert.AlertType.ERROR, "Program not found!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Program not found!").show();
            e.printStackTrace();
        }
    }

}
