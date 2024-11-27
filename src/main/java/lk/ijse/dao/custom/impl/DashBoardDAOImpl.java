package lk.ijse.dao.custom.impl;

import lk.ijse.comfit.FactoryConfiguration;
import lk.ijse.dao.custom.DashBoardDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashBoardDAOImpl implements DashBoardDAO {
    public Map<String, Double> getPaymentsByDay() throws IOException {
        Map<String, Double> paymentsByDay = new HashMap<>();

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            String hql = "SELECT p.pay_date, SUM(p.pay_amount) FROM Payment p GROUP BY p.pay_date";
            List<Object[]> results = session.createQuery(hql).list();

            for (Object[] result : results) {
                String date = (String) result[0];
                Double totalAmount = (Double) result[1];
                paymentsByDay.put(date, totalAmount);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }

        return paymentsByDay;
    }

    @Override
    public Map<String, Long> getStudentCountByCourse() throws IOException {
            Session session = FactoryConfiguration.getInstance().getSession();
            Transaction transaction = session.beginTransaction();

            List<Object[]> results = session.createQuery("SELECT c.programName, COUNT(r.student.id) " + "FROM Registration r JOIN r.program c " + "GROUP BY c.programName").list();

            transaction.commit();
            session.close();

            Map<String, Long> studentsByCourse = new HashMap<>();
            for (Object[] result : results) {
                studentsByCourse.put((String) result[0], (Long) result[1]);
            }

            return studentsByCourse;
        }

    @Override
    public int getStudentCount() throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        int studentCount = 0;

        try {
            transaction = session.beginTransaction();

            Long count = (Long) session.createQuery("SELECT COUNT(s.id) FROM Student s").uniqueResult();

            if (count != null) {
                studentCount = count.intValue();
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }

        return studentCount;
    }

    @Override
    public int getProgramCount() throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        int programCount = 0;

        try {
            transaction = session.beginTransaction();

            Long count = (Long) session.createQuery("SELECT COUNT(p.id) FROM Program p").uniqueResult();

            if (count != null) {
                programCount = count.intValue();
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }

        return programCount;
    }

    @Override
    public int getPaymentCount() throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        int paymentCount = 0;

        try {
            transaction = session.beginTransaction();

            Long count = (Long) session.createQuery("SELECT COUNT(p.id) FROM Payment p").uniqueResult();

            if (count != null) {
                paymentCount = count.intValue();
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }

        return paymentCount;
    }
}
