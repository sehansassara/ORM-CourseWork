package lk.ijse.dao.custom.impl;

import lk.ijse.comfit.FactoryConfiguration;
import lk.ijse.dao.custom.PaymentDAO;
import lk.ijse.entity.Payment;
import lk.ijse.entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {
    @Override
    public String generateId() throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        String nextId = "";
        try {
            String lastId = (String) session.createQuery("SELECT s.pay_id FROM Payment s ORDER BY s.pay_id DESC")
                    .setMaxResults(1)
                    .uniqueResult();

            if (lastId != null) {
                String prefix = "P";
                String[] split = lastId.split(prefix);

                int idNum = Integer.parseInt(split[1]);
                nextId = prefix + String.format("%03d", ++idNum);
            } else {
                nextId = "P001";
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return nextId;
    }

    @Override
    public List<Payment> getAll() throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        List<Payment> payments = session.createQuery("from Payment ").list();
        transaction.commit();
        session.close();
        return payments;
    }

    @Override
    public boolean delete(Payment batchDTO) throws SQLException, ClassNotFoundException, IOException {
        return false;
    }

    @Override
    public boolean save(Payment batchDTO) throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.save(batchDTO);

        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Payment batchDTO) throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.update(batchDTO);

        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public Payment searchById(String id) throws IOException {
        return null;
    }

    @Override
    public double getTotalAmount(String stuid) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        String sql = "SELECT SUM(paymentAmount) FROM registration WHERE student_id = :studentId GROUP BY student_id";

        Query<Double> query = session.createNativeQuery(sql, Double.class);
        query.setParameter("studentId", stuid);

        Double totalPayment = query.uniqueResult();

        return (totalPayment != null) ? totalPayment : 0.0;
    }
}
