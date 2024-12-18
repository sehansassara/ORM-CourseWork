package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.entity.Payment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface PaymentBo extends SuperBO {
    String generatePayId();

    double getTotalPaymentForStudent(String stuid) throws IOException;

    boolean savePayment(PaymentDTO paymentDTO) throws SQLException, IOException, ClassNotFoundException;

    List<PaymentDTO> getAllPayments();

    boolean updatePayment(PaymentDTO paymentDTO) throws SQLException, IOException, ClassNotFoundException;

    PaymentDTO searchByPaymentId(String id) throws IOException;

    List<Payment> searchByStuIdPay(String id) throws IOException;
}
