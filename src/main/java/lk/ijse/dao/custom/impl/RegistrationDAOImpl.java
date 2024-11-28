package lk.ijse.dao.custom.impl;

import lk.ijse.comfit.FactoryConfiguration;
import lk.ijse.dao.custom.RegistrationDAO;
import lk.ijse.entity.Program;
import lk.ijse.entity.Registration;
import lk.ijse.entity.Student;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDAOImpl implements RegistrationDAO {
    @Override
    public String generateId() throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        String nextId = "";
        try {
            String lastId = (String) session.createQuery("SELECT s.id FROM Registration s ORDER BY s.id DESC")
                    .setMaxResults(1)
                    .uniqueResult();

            if (lastId != null) {
                String prefix = "R";
                String[] split = lastId.split(prefix);

                int idNum = Integer.parseInt(split[1]);
                nextId = prefix + String.format("%03d", ++idNum);
            } else {
                nextId = "R001";
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
    public List<Registration> getAll() throws SQLException, ClassNotFoundException, IOException {
        return List.of();
    }

    @Override
    public boolean delete(Registration batchDTO) throws SQLException, ClassNotFoundException, IOException {
        return false;
    }

    @Override
    public boolean save(Registration batchDTO) throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.save(batchDTO);

        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Registration batchDTO) throws SQLException, ClassNotFoundException, IOException {
        return false;
    }

    @Override
    public Registration searchById(String id) throws IOException {
        return null;
    }

    @Override
    public List<String> getProgramIds() throws HibernateException, IOException {
        List<String> programIds = new ArrayList<>();
        Session session = null;
        Transaction transaction = null;

        try {
            session = FactoryConfiguration.getInstance().getSession();

            transaction = session.beginTransaction();

            String hql = "SELECT p.programName FROM Program p";
            Query<String> query = session.createQuery(hql, String.class);

            programIds = query.list();

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new HibernateException("Error fetching program IDs", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return programIds;
    }

    @Override
    public Student getUserById(String stuId) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            String hql = "FROM Student WHERE id = :stuId";

            Query<Student> query = session.createQuery(hql, Student.class);
            query.setParameter("stuId", stuId);

            Student student = query.uniqueResult();

            transaction.commit();
            return student;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    @Override
    public Program getProgramById(String proId) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            String hql = "FROM Program WHERE id = :id";

            Query<Program> query = session.createQuery(hql, Program.class);
            query.setParameter("id", proId);

            Program program = query.uniqueResult();

            transaction.commit();
            return program;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    @Override
    public Registration getRegById(String regId) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            String hql = "FROM Registration WHERE id = :id";

            Query<Registration> query = session.createQuery(hql, Registration.class);
            query.setParameter("id", regId);

            Registration registration = query.uniqueResult();

            transaction.commit();
            return registration;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    public boolean isStudentRegisteredForProgram(String studentId, String programId) {
        String hql = "SELECT COUNT(r) FROM Registration r WHERE r.student.id = :studentId AND r.program.id = :programId";

        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("studentId", studentId);
            query.setParameter("programId", programId);

            Long count = query.uniqueResult();

            return count != null && count > 0;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }



}
