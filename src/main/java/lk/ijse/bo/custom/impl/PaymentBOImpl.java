package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.PaymentBo;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.PaymentDAO;
import lk.ijse.dao.custom.RegistrationDAO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.entity.Payment;
import lk.ijse.entity.Student;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentBOImpl implements PaymentBo {
    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PAYMENTS);
    @Override
    public String generatePayId() {
        try {
            return paymentDAO.generateId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double getTotalPaymentForStudent(String stuid) throws IOException {
        return paymentDAO.getTotalAmount(stuid);
    }

    @Override
    public boolean savePayment(PaymentDTO paymentDTO) throws SQLException, IOException, ClassNotFoundException {
        return paymentDAO.save(new Payment(paymentDTO.getPay_id(),paymentDTO.getPay_date(),paymentDTO.getBalance_amount(),paymentDTO.getPay_amount(),paymentDTO.getUpfront_amount(),paymentDTO.getStudent(),paymentDTO.getRegistration()));
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        List<PaymentDTO> paymentDTOS = new ArrayList<>();
        List<Payment> payments = null;
        try {
            payments = paymentDAO.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Payment payment : payments) {
            paymentDTOS.add(new PaymentDTO(payment.getPay_id(),payment.getPay_date(),payment.getBalance_amount(),payment.getPay_amount(),payment.getUpfront_amount(),payment.getStudent(),payment.getRegistration()));
        }
        return paymentDTOS;
    }

    @Override
    public boolean updatePayment(PaymentDTO paymentDTO) throws SQLException, IOException, ClassNotFoundException {
        return paymentDAO.update(new Payment(paymentDTO.getPay_id(),paymentDTO.getPay_date(),paymentDTO.getBalance_amount(),paymentDTO.getPay_amount(),paymentDTO.getUpfront_amount(),paymentDTO.getStudent(),paymentDTO.getRegistration()));

    }

    @Override
    public PaymentDTO searchByPaymentId(String id) throws IOException {
        Payment payment = paymentDAO.searchById(id);

        return new PaymentDTO(payment.getPay_id(),payment.getPay_date(),payment.getBalance_amount(),payment.getPay_amount(),payment.getUpfront_amount(),payment.getStudent(),payment.getRegistration());
    }

    @Override
    public List<Payment> searchByStuIdPay(String id) throws IOException {
        return paymentDAO.searchByStuIdPay(id);
    }
}
