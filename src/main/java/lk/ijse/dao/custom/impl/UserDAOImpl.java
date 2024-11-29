package lk.ijse.dao.custom.impl;

import lk.ijse.comfit.FactoryConfiguration;
import lk.ijse.dao.custom.UserDAO;
import lk.ijse.entity.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    @Override
    public String generateId() throws SQLException, ClassNotFoundException, IOException {
            Session session = FactoryConfiguration.getInstance().getSession();
            Transaction transaction = session.beginTransaction();

            String nextId = "";
            try {
                String lastId = (String) session.createQuery("SELECT u.user_id FROM User u ORDER BY u.user_id DESC")
                        .setMaxResults(1)
                        .uniqueResult();

                if (lastId != null) {
                    String prefix = "U";
                    String[] split = lastId.split(prefix);

                    int idNum = Integer.parseInt(split[1]);
                    nextId = prefix + String.format("%03d", ++idNum);
                } else {
                    nextId = "U001";
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
    public List<User> getAll() throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        List<User> users = session.createQuery("from User").list();
        transaction.commit();
        session.close();
        return users;
    }

    @Override
    public boolean delete(User batchDTO) throws SQLException, ClassNotFoundException, IOException {
        Session session = null;
        Transaction transaction = null;

        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();

            String id = "U000";
            String hql = "UPDATE Student SET user = id WHERE user.id = :userId";
            Query query = session.createQuery(hql);
            query.setParameter("userId", batchDTO.getUser_id());
            query.executeUpdate();

            session.delete(batchDTO);

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new IOException("Failed to delete user", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean save(User batchDTO) throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.save(batchDTO);

        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(User batchDTO) throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.update(batchDTO);

        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public User searchById(String id) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        Object user = session.createQuery("SELECT u FROM User u WHERE u.user_id = :id")
                .setParameter("id", id).uniqueResult();
        transaction.commit();
        session.close();
        return (User) user;
    }

    @Override
    public User getUserByName(String userName) {
        Transaction transaction = null;
        User user = null;

        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();

            Query<User> query = session.createQuery("FROM User u WHERE u.user_name = :username", User.class);
            query.setParameter("username", userName);
            user = query.uniqueResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User getUserById(String userId) {
        Transaction transaction = null;
        User user = null;

        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();

            Query<User> query = session.createQuery("FROM User u WHERE u.user_id = :userId", User.class);
            query.setParameter("userId", userId);
            user = query.uniqueResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public boolean updatePass(String newPassword, String userName) {
        Transaction transaction = null;

        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();

            String hql = "UPDATE User u SET u.password = :newPassword WHERE u.user_name = :userName";
            Query query = session.createQuery(hql);
            query.setParameter("newPassword", newPassword);
            query.setParameter("userName", userName);

            int rowsAffected = query.executeUpdate();

            transaction.commit();

            return rowsAffected > 0;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getUserCount() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Long count = (Long) session.createQuery("SELECT COUNT(u) FROM User u").uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }



}
