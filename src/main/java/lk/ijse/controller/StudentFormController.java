package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.StudentBo;
import lk.ijse.bo.custom.UserBo;
import lk.ijse.controller.util.Regex;
import lk.ijse.dto.ProgramDTO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.dto.UserDTO;
import lk.ijse.entity.Registration;
import lk.ijse.entity.Student;
import lk.ijse.entity.User;
import lk.ijse.tm.StudentTm;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class StudentFormController {

    @FXML
    private AnchorPane anchorStudent;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colAddress1;

    @FXML
    private TableColumn<?, ?> colAdmin;

    @FXML
    private TableColumn<?, ?> colAdmin1;

    @FXML
    private TableColumn<?, ?> colPrograms;

    @FXML
    private TableColumn<?, ?> colStudentEmail;

    @FXML
    private TableColumn<?, ?> colStudentEmail1;

    @FXML
    private TableColumn<?, ?> colStudentId;

    @FXML
    private TableColumn<?, ?> colStudentId1;

    @FXML
    private TableColumn<?, ?> colStudentName;

    @FXML
    private TableColumn<?, ?> colStudentName1;

    @FXML
    private TableColumn<?, ?> colStudentTel;

    @FXML
    private TableColumn<?, ?> colStudentTel1;

    @FXML
    private TableView<StudentTm> tblStudent;

    @FXML
    private TableView<StudentTm> tblStudent1;

    @FXML
    private TextField txtStudentAddress;

    @FXML
    private TextField txtStudentEmail;

    @FXML
    private TextField txtStudentId;

    @FXML
    private TextField txtStudentName;

    @FXML
    private TextField txtStudentTel;

    @FXML
    private TextField txtUserId;

    StudentBo studentBo = (StudentBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);
    UserBo userBo = (UserBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);

    public void initialize() throws SQLException, IOException, ClassNotFoundException {
        loadAllStudent();
        setCellValueFactory();
        generateNextStudentId();
        clearFields();
       /* loadStudentsRegisteredForAllCulinaryPrograms();*/

        String userId = SessionManager.getUserId();

        if (userId != null) {
            txtUserId.setText(userId);
        } else {
            txtUserId.setText("No User ID available");
        }
    }


   /* private void loadStudentsRegisteredForAllCulinaryPrograms() throws IOException {
        ObservableList<StudentTm> obList = FXCollections.observableArrayList();
        List<StudentDTO> studentList = studentBo.getStudentsRegisteredForAllCulinaryPrograms();
        // Debugging output
        System.out.println("Number of students retrieved: " + studentList.size());
        for (StudentDTO studentDTO : studentList) {
            String userId = studentDTO.getUser() != null ? studentDTO.getUser().getUser_id() : null;
            StudentTm studentTm = new StudentTm(
                    studentDTO.getId(),
                    userId,
                    studentDTO.getName(),
                    studentDTO.getPhoneNumber(),
                    studentDTO.getEmail(),
                    studentDTO.getAddress()
            );
            obList.add(studentTm);
        }
        tblStudent1.setItems(obList);
    }*/



    private void clearFields() {
        txtUserId.setText("");
        txtStudentName.setText("");
        txtStudentTel.setText("");
        txtStudentEmail.setText("");
        txtStudentAddress.setText("");
    }

    private String generateNextStudentId() {
        String id = studentBo.generateStudentId();
        txtStudentId.setText(id);
        return id;
    }

    private void setCellValueFactory() {
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStudentEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colStudentTel.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colAdmin.setCellValueFactory(new PropertyValueFactory<>("user"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
    }

    private void loadAllStudent() {
        ObservableList<StudentTm> obList = FXCollections.observableArrayList();
        List<StudentDTO> studentDTOList = studentBo.getAllStudent();
        for (StudentDTO studentDTO : studentDTOList) {
            String userId = studentDTO.getUser() != null ? studentDTO.getUser().getUser_id() : null;
            StudentTm studentTm = new StudentTm(
                    studentDTO.getId(),
                    userId,
                    studentDTO.getName(),
                    studentDTO.getPhoneNumber(),
                    studentDTO.getEmail(),
                    studentDTO.getAddress()
            );
            obList.add(studentTm);
        }
        tblStudent.setItems(obList);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtStudentId.getText();
        String name = txtStudentName.getText();
        String phoneNumber = txtStudentTel.getText();
        String email = txtStudentEmail.getText();
        String address = txtStudentAddress.getText();
        String uId = txtUserId.getText();

        User user = userBo.getUserById(uId);

        StudentDTO student = new StudentDTO(id, name, phoneNumber, email, address, user);

        try {
            if (studentBo.deleteStudent(student)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Student deleted Successfully!").show();
                generateNextStudentId();
                clearFields();
                loadAllStudent();
            } else {
                new Alert(Alert.AlertType.ERROR, "SQL Error").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtStudentId.getText();
        String name = txtStudentName.getText();
        String phoneNumber = txtStudentTel.getText();
        String email = txtStudentEmail.getText();
        String address = txtStudentAddress.getText();
        String uId = txtUserId.getText();

        User user = userBo.getUserById(uId);

        StudentDTO student = new StudentDTO(id, name, phoneNumber, email, address, user);

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        try {
            if (studentBo.saveStudent(student)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Student Added Successfully!").show();
                generateNextStudentId();
                clearFields();
                loadAllStudent();

                openRegistrationForm(student);
            } else {
                new Alert(Alert.AlertType.ERROR, "SQL Error").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void openRegistrationForm(StudentDTO student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registration_form.fxml"));
            AnchorPane registrationForm = loader.load();
            RegistrationFormController registrationFormController = loader.getController();
           /* paymentFormController.setNetTotal(netTotal);*/
            registrationFormController.setStudentData(student);
            anchorStudent.getChildren().clear();
            anchorStudent.getChildren().add(registrationForm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtStudentId.getText();
        String name = txtStudentName.getText();
        String phoneNumber = txtStudentTel.getText();
        String email = txtStudentEmail.getText();
        String address = txtStudentAddress.getText();
        String uId = txtUserId.getText();

        User user = userBo.getUserById(uId);

        StudentDTO student = new StudentDTO(id, name, phoneNumber, email, address, user);

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        try {
            if (studentBo.updateStudent(student)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Student Updated Successfully!").show();
                generateNextStudentId();
                clearFields();
                loadAllStudent();
            } else {
                new Alert(Alert.AlertType.ERROR, "SQL Error").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void tblStudentOnMouse(MouseEvent event) {
        int index = tblStudent.getSelectionModel().getSelectedIndex();

        if (index <= -1){
            return;
        }

        String id = colStudentId.getCellData(index).toString();
        String userId = colAdmin.getCellData(index).toString();
        String name = colStudentName.getCellData(index).toString();
        String email = colStudentEmail.getCellData(index).toString();
        String tel = colStudentTel.getCellData(index).toString();
        String address = colAddress.getCellData(index).toString();


        txtUserId.setText(userId);
        txtStudentId.setText(id);
        txtStudentName.setText(name);
        txtStudentTel.setText(tel);
        txtStudentEmail.setText(email);
        txtStudentAddress.setText(address);
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        try {
            String id = txtStudentId.getText();
            StudentDTO studentDTO = studentBo.searchByStudentId(id);

            if (studentDTO != null) {
                txtStudentId.setText(studentDTO.getId());
                txtUserId.setText(String.valueOf(studentDTO.getUser()));
                txtStudentName.setText(studentDTO.getName());
                txtStudentEmail.setText(studentDTO.getEmail());
                txtStudentAddress.setText(studentDTO.getAddress());
                txtStudentTel.setText(studentDTO.getPhoneNumber());
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

    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.controller.util.TextField.ID,txtStudentId)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.util.TextField.NAME,txtStudentName)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.util.TextField.ADDRESS,txtStudentAddress)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.util.TextField.EMAIL,txtStudentEmail)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.util.TextField.CONTACT,txtStudentTel)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.util.TextField.ID,txtUserId)) return false;
        return true;
    }

    @FXML
    void txtStAddOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.util.TextField.ADDRESS,txtStudentAddress);
    }

    @FXML
    void txtStEmOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.util.TextField.EMAIL,txtStudentEmail);
    }

    @FXML
    void txtStIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.util.TextField.ID,txtStudentId);
    }

    @FXML
    void txtStNaOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.util.TextField.NAME,txtStudentName);
    }

    @FXML
    void txtStTeOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.util.TextField.CONTACT,txtStudentTel);
    }

    @FXML
    void txtUIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.util.TextField.ID,txtUserId);
    }
}
