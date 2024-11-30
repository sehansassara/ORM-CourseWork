package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.PaymentBo;
import lk.ijse.bo.custom.RegistrationBo;
import lk.ijse.bo.custom.StudentBo;
import lk.ijse.controller.util.Regex;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.entity.Payment;
import lk.ijse.entity.Registration;
import lk.ijse.entity.Student;
import lk.ijse.tm.PaymentTm;

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
    private TableColumn<?, ?> colRegId;

    @FXML
    private Label lblPayAmount;

    @FXML
    private Label lblupfrontPay;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblPayId;

    @FXML
    private Label lblRegId;

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
    RegistrationBo registrationBo = (RegistrationBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.REGISTRATION);

    public void initialize() throws SQLException, IOException, ClassNotFoundException {
        generatePayId();
        setDate();
        loadPayments();
        setCellValueFactory();
        clearFields();
    }

    private void clearFields() {
        txtAmount.setText("");
        txtPayAmount.setText("");
        txtStudentId.setText("");

    }

    private void setCellValueFactory() {
        colStuId.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("balance_amount"));
        colPayAmount.setCellValueFactory(new PropertyValueFactory<>("pay_amount"));
        colUpfrontPay.setCellValueFactory(new PropertyValueFactory<>("upfront_amount"));
        colPayId.setCellValueFactory(new PropertyValueFactory<>("pay_id"));
        colRegId.setCellValueFactory(new PropertyValueFactory<>("regId"));

    }

    private void loadPayments() {
        ObservableList<PaymentTm> obList = FXCollections.observableArrayList();
        List<PaymentDTO> paymentDTOList = paymentBo.getAllPayments();
        System.out.println("Payment DTO List Size: " + paymentDTOList.size());

        for (PaymentDTO paymentDTO : paymentDTOList) {
            System.out.println("PaymentDTO: " + paymentDTO.getPay_id() + " - " + paymentDTO.getPay_date());
            System.out.println(paymentDTO.getPay_id() + " - " + paymentDTO.getPay_date()+"_"+paymentDTO.getPay_amount()+"_"+paymentDTO.getPay_amount());
            String stuId = paymentDTO.getStudent() != null ? paymentDTO.getStudent().getId() : null;
            PaymentTm paymentTm = new PaymentTm(
                    paymentDTO.getPay_id(),
                    paymentDTO.getPay_date(),
                    paymentDTO.getBalance_amount(),
                    paymentDTO.getPay_amount(),
                    paymentDTO.getUpfront_amount(),
                    stuId,
                    paymentDTO.getRegistration().getId()
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
    void btnPayOnAction(ActionEvent event) throws IOException {
        String payid = lblPayId.getText();
        String date = lblDate.getText();
        double totalAmount = Double.parseDouble(txtAmount.getText());
        double payAmount = Double.parseDouble(txtPayAmount.getText());
        double upFrontPay = Double.parseDouble(lblupfrontPay.getText());
        String stuId = txtStudentId.getText();
        String regId = lblRegId.getText();

        Student student = studentBo.getStudentById(stuId);
        Registration registration = registrationBo.getRegById(regId);

        PaymentDTO paymentDTO = new PaymentDTO(payid, date, totalAmount, payAmount, upFrontPay, student, registration);

        if (!isValied()) {
            new Alert(Alert.AlertType.ERROR, "Please check all fields.").show();
            return;
        }

        try {
            if (paymentBo.savePayment(paymentDTO)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Payment Added Successfully!").show();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/student_form.fxml"));
                AnchorPane registrationForm = loader.load();
                anchorRegister.getChildren().clear();
                anchorRegister.getChildren().add(registrationForm);
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

    double ss;
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
        String regId = colRegId.getCellData(index).toString();

        txtStudentId.setText(id);
        txtAmount.setText(amount);
        txtPayAmount.setText(payAmount);
        lblupfrontPay.setText(upFrontPay);
        lblPayId.setText(payId);
        lblRegId.setText(regId);
    }

    /*@FXML
    void txtSearchOnAction(ActionEvent event) {
        try {
            String id = txtStudentId.getText();
            PaymentDTO paymentDTO = paymentBo.searchByPaymentId(id);

            if (paymentDTO != null) {
                txtStudentId.setText(String.valueOf(paymentDTO.getStudent().getId()));
                txtAmount.setText(String.valueOf(paymentDTO.getBalance_amount()));

            *//*    txtPayAmount.setText(String.valueOf(paymentDTO.getPay_amount()));*//*

                lblPayId.setText(paymentDTO.getPay_id());
                lblupfrontPay.setText(String.valueOf(paymentDTO.getUpfront_amount()));
                lblRegId.setText(paymentDTO.getRegistration().getId());
                ss= Double.parseDouble((String.valueOf(paymentDTO.getPay_amount())));

            } else {
                new Alert(Alert.AlertType.INFORMATION, "Payment not found!").show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        try {
            String studentId = txtStudentId.getText();

            List<Payment> payments = paymentBo.searchByStuIdPay(studentId);

            if (payments == null || payments.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "No payments found for the student!").show();
                return;
            }

            laodPayments(payments);

            if (payments.size() == 1) {
                Payment payment = payments.get(0);
                lblPayId.setText(payment.getPay_id());
                lblupfrontPay.setText(String.valueOf(payment.getUpfront_amount()));
                lblRegId.setText(payment.getRegistration().getId());
                ss = payment.getPay_amount();
                txtAmount.setText(String.valueOf(payment.getBalance_amount()));
                txtStudentId.setText(payment.getStudent().getId());
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void laodPayments(List<Payment> paymentList) {
        ObservableList<PaymentTm> obList = FXCollections.observableArrayList();

        for (Payment p : paymentList) {
            PaymentTm tm = new PaymentTm(
                    p.getPay_id(),
                    p.getPay_date(),
                    p.getBalance_amount(),
                    p.getPay_amount(),
                    p.getUpfront_amount(),
                    p.getStudent().getId(),
                    p.getRegistration().getId()
            );
            obList.add(tm);
        }
        tblPayment.setItems(obList);
        setCellValueFactory();
    }


    @FXML
    void btnPaymentEditOnAction(ActionEvent event) throws IOException {
        String payid = lblPayId.getText();
        String date = lblDate.getText();
        double totalAmount = Double.parseDouble(txtAmount.getText());
        double payAmount = ss;
        double upFrontPay = Double.parseDouble(lblupfrontPay.getText());
        String stuId = txtStudentId.getText();
        String regId = lblRegId.getText();

        double payAmount1 = Double.parseDouble(txtPayAmount.getText());
        double netPayAmount = payAmount1+payAmount;
        double upFrontPay1 = totalAmount-netPayAmount;

        Student student = studentBo.getStudentById(stuId);
        Registration registration = registrationBo.getRegById(regId);

        PaymentDTO paymentDTO = new PaymentDTO(payid, date,  totalAmount,netPayAmount,  upFrontPay1, student, registration);

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

    public void setRegistrationData(String stuid, String regId, double amount) {
        txtStudentId.setText(stuid);
        lblRegId.setText(regId);

        try {
            double totalPayment = amount;
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
