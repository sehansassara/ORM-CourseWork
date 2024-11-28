package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.entity.Payment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface PaymentDAO extends CrudDAO<Payment> {
    double getTotalAmount(String stuid) throws IOException;

    List<Payment> searchByStuIdPay(String id) throws IOException;
}
