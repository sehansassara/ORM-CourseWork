package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.ProgramBo;
import lk.ijse.bo.custom.RegistrationBo;
import lk.ijse.dto.ProgramDTO;
import lk.ijse.dto.RegistrationDTO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.entity.Program;
import lk.ijse.entity.Student;
import lk.ijse.tm.RegistrationTm;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class RegistrationFormController {

    @FXML
    private AnchorPane anchorRegister;

    @FXML
    private JFXComboBox<String> cmbPrograms;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colFee;

    @FXML
    private TableColumn<?, ?> colProId;

    @FXML
    private TableColumn<?, ?> colRegId;

    @FXML
    private TableColumn<?, ?> colRemove;

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
    private TableView<RegistrationTm> tblCart;

    @FXML
    private TextField txtStudentId;

    @FXML
    private TableColumn<?, ?> colPrograms;

    private ObservableList<RegistrationTm> obList = FXCollections.observableArrayList();


    ProgramBo programBo = (ProgramBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PROGRAM);
    RegistrationBo registrationBo = (RegistrationBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.REGISTRATION);


    public void initialize() throws SQLException, IOException, ClassNotFoundException {
        generateRegId();
        setDate();
        getProgramsNames();
        setCellValueFactory();
        clearfields();
    }

    private void clearfields() {
        txtStudentId.setText("");
        lblProgramID.setText("");
        lblStudentName.setText("");
        lblTotalAmount.setText("");
        lblProgramFee.setText("");
        lblProgramDuration.setText("");
    }

    private void setCellValueFactory() {
        colProId.setCellValueFactory(new PropertyValueFactory<>("couId"));
        colRegId.setCellValueFactory(new PropertyValueFactory<>("regId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        colRemove.setCellValueFactory(new PropertyValueFactory<>("remove"));
        colStuId.setCellValueFactory(new PropertyValueFactory<>("stuId"));
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

    @FXML
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

    }

    private double calculateTotalFee() {
        double total = 0.0;

        for (RegistrationTm registrationTm : obList) {
            total += registrationTm.getFee();
        }

        return total;
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws IOException {
        String regId = lblRegId.getText();
        String stuId = txtStudentId.getText();
        String proId = lblProgramID.getText();
        double totalFee = calculateTotalFee();
        String regDate = lblDate.getText();
        double amount = Double.parseDouble(lblTotalAmount.getText());


        Student student = registrationBo.getStudentById(stuId);
        Program program = registrationBo.getProgramDetailsById(proId);

        RegistrationDTO registrationDTO = new RegistrationDTO (regId, program, student, regDate, amount);

        try {
            if (registrationBo.saveregisterdStudent(registrationDTO)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Registration Successfully!").show();
                generateRegId();
                clearfields();
            } else {
                new Alert(Alert.AlertType.ERROR, "SQL Error").show();
            }
        } catch (SQLException e) {

        } catch (IOException e) {

        } catch (ClassNotFoundException e) {

        }

    }

    @FXML
    void btnViewOnAction(ActionEvent event) {

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
