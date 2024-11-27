package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.PaymentBo;
import lk.ijse.bo.custom.ProgramBo;
import lk.ijse.bo.custom.StudentBo;
import lk.ijse.controller.util.Regex;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.entity.Student;
import lk.ijse.entity.User;
import lk.ijse.tm.PaymentTm;
import lk.ijse.tm.StudentTm;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PaymentFormController {

    @FXML
    private AnchorPane anchorRegister;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colPayAmount;

    @FXML
    private TableColumn<?, ?> colPayId;

    @FXML
    private TableColumn<?, ?> colStuId;

    @FXML
    private TableColumn<?, ?> colUpfrontPay;

    @FXML
    private Label lblupfrontPay;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblPayId;

    @FXML
    private Label lblStudentName;

    @FXML
    private TableView<PaymentTm> tblPayment;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtPayAmount;

    @FXML
    private TextField txtStudentId;

    @FXML
    private TextField txtupfrontPay;

    PaymentBo paymentBo = (PaymentBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PAYMENT);
    StudentBo studentBo = (StudentBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);

    public void initialize() throws SQLException, IOException, ClassNotFoundException {
        generatePayId();
        setDate();
        loadPayments();
        setCellValueFactory();
//        getProgramsNames();
//        loadStudentsRegisteredForAllCulinaryPrograms();
//        setCellValueFactory();
//        clearfields();
    }

    private void setCellValueFactory() {
        colStuId.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("balance_amount"));
        colPayAmount.setCellValueFactory(new PropertyValueFactory<>("pay_amount"));
        colUpfrontPay.setCellValueFactory(new PropertyValueFactory<>("upfront_amount"));
        colPayId.setCellValueFactory(new PropertyValueFactory<>("pay_id"));

    }

    private void loadPayments() {
        ObservableList<PaymentTm> obList = FXCollections.observableArrayList();
        List<PaymentDTO> paymentDTOList = paymentBo.getAllPayments();
        System.out.println("Payment DTO List Size: " + paymentDTOList.size());  // Debugging line

        for (PaymentDTO paymentDTO : paymentDTOList) {
            System.out.println("PaymentDTO: " + paymentDTO.getPay_id() + " - " + paymentDTO.getPay_date());  // Debugging line
            String stuId = paymentDTO.getStudent() != null ? paymentDTO.getStudent().getId() : null;
            PaymentTm paymentTm = new PaymentTm(
                    paymentDTO.getPay_id(),
                    paymentDTO.getPay_date(),
                    paymentDTO.getBalance_amount(),
                    paymentDTO.getPay_amount(),
                    paymentDTO.getUpfront_amount(),
                    stuId
            );
            obList.add(paymentTm);
        }

        tblPayment.setItems(obList);
    }


    private String generatePayId() {
        String id = paymentBo.generatePayId();
        lblPayId.setText(id);
        return id;
    }


    private void setDate() {
        LocalDate currentDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = currentDate.format(formatter);

        lblDate.setText(formattedDate);
    }

    @FXML
    void btnPayOnAction(ActionEvent event) {
        String payid = lblPayId.getText();
        String date = lblDate.getText();
        double totalAmount = Double.parseDouble(txtAmount.getText());
        double payAmount = Double.parseDouble(txtPayAmount.getText());
        double upFrontPay = Double.parseDouble(lblupfrontPay.getText());
        String stuId = txtStudentId.getText();

        Student student = studentBo.getStudentById(stuId);

        PaymentDTO paymentDTO = new PaymentDTO(payid, date, totalAmount, payAmount, upFrontPay, student);

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        try {
            if (paymentBo.savePayment(paymentDTO)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Payment Added Successfully!").show();
                generatePayId();
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
    void tblUserOnMouse(MouseEvent event) {
        int index = tblPayment.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }

        String id = colStuId.getCellData(index).toString();
        String amount = colAmount.getCellData(index).toString();
        String payAmount = colPayAmount.getCellData(index).toString();
        String upFrontPay = colUpfrontPay.getCellData(index).toString();
        String payId = colPayId.getCellData(index).toString();

        txtStudentId.setText(id);
        txtAmount.setText(amount);
        txtPayAmount.setText(payAmount);
        lblupfrontPay.setText(upFrontPay);
        lblPayId.setText(payId);
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {

    }

    @FXML
    void btnPaymentEditOnAction(ActionEvent event) {
        String payid = lblPayId.getText();
        String date = lblDate.getText();
        double totalAmount = Double.parseDouble(txtAmount.getText());
        double payAmount = Double.parseDouble(txtPayAmount.getText());
        double upFrontPay = Double.parseDouble(lblupfrontPay.getText());
        String stuId = txtStudentId.getText();

        Student student = studentBo.getStudentById(stuId);

        PaymentDTO paymentDTO = new PaymentDTO(payid, date, totalAmount, payAmount, upFrontPay, student);

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        try {
            if (paymentBo.updatePayment(paymentDTO)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Payment Added Successfully!").show();
                generatePayId();
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

    public void setRegistrationData(String stuid) {
        txtStudentId.setText(stuid);

        try {
            double totalPayment = paymentBo.getTotalPaymentForStudent(stuid);
            txtAmount.setText(String.valueOf(totalPayment));

            txtPayAmount.setText("");

            txtPayAmount.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.isEmpty()) {
                    try {
                        double payAmount = Double.parseDouble(newValue);

                        double upfrontPay = totalPayment - payAmount;

                        if (upfrontPay < 0) {
                            lblupfrontPay.setText("Invalid Pay Amount");
                        } else {
                            lblupfrontPay.setText(String.valueOf(upfrontPay));
                        }
                    } catch (NumberFormatException e) {
                        lblupfrontPay.setText("Error");
                    }
                } else {
                    lblupfrontPay.setText("0");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            txtAmount.setText("Error");
            txtPayAmount.setText("Error");
            txtupfrontPay.setText("Error");
        }
    }

    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.controller.util.TextField.PRICE,txtAmount)) return false;
        if (!Regex.setTextColor(lk.ijse.controller.util.TextField.PRICE,txtPayAmount)) return false;
        return true;
    }

    @FXML
    void txtAmountOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.util.TextField.PRICE,txtAmount);
    }

    @FXML
    void txtPayAmountOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(lk.ijse.controller.util.TextField.PRICE,txtPayAmount);
    }

}
