package lk.ijse.dao.custom.impl;

import jakarta.persistence.EntityManager;
import lk.ijse.comfit.FactoryConfiguration;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.dto.RegistrationDTO;
import lk.ijse.entity.Program;
import lk.ijse.entity.Registration;
import lk.ijse.entity.Student;
import lk.ijse.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class StudentDAOImpl implements StudentDAO {
    @Override
    public String generateId() throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        String nextId = "";
        try {
            String lastId = (String) session.createQuery("SELECT s.id FROM Student s ORDER BY s.id DESC")
                    .setMaxResults(1)
                    .uniqueResult();

            if (lastId != null) {
                String prefix = "S";
                String[] split = lastId.split(prefix);

                int idNum = Integer.parseInt(split[1]);
                nextId = prefix + String.format("%03d", ++idNum);
            } else {
                nextId = "S001";
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
    public List<Student> getAll() throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        List<Student> students = session.createQuery("from Student").list();
        transaction.commit();
        session.close();
        return students;
    }

    @Override
    public boolean delete(Student batchDTO) throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.delete(batchDTO);

        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean save(Student batchDTO) throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.save(batchDTO);

        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Student batchDTO) throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.update(batchDTO);

        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public Student searchById(String id) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        Object student = session.createQuery("SELECT p FROM Student p WHERE p.id = :id")
                .setParameter("id", id).uniqueResult();
        transaction.commit();
        session.close();
        return (Student) student;
    }


    @Override
    public List<Student> getStudentsRegisteredForAllCulinaryPrograms() throws IOException {
        List<Student> students = new ArrayList<>();
        Session session = null;
        Transaction transaction = null;

        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();

            /*String hql = "SELECT s FROM Student s JOIN s.registrations r JOIN r.program p " +
                    "WHERE p.programName IN ('Professional Cooking', 'Baking & Pastry Arts', 'International Cuisine', 'Culinary Management') " +
                    "GROUP BY s HAVING COUNT(DISTINCT p.id) = 2";

            Query<Student> query = session.createQuery(hql, Student.class);*/
            String hql = "SELECT s.* " +
                    "FROM Student s " +
                    "JOIN Registration r ON s.id = r.student_id " +
                    "JOIN Program p ON r.program_id = p.id " +
                    "WHERE p.programName IN ('Professional Cooking', 'Baking & Pastry Arts', 'International Cuisine', 'Culinary Management') " +
                    "GROUP BY s.id " +
                    "HAVING COUNT(DISTINCT p.id) = 2";

            Query<Student> query = session.createNativeQuery(hql, Student.class);
            students = query.getResultList();
            // Debugging output
            System.out.println("Number of students retrieved: " + students.size());

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new IOException("Error retrieving students registered for all culinary programs", e);
        } finally {
            if (session != null) session.close(); // Close session to prevent memory leaks
        }

        return students;
    }

    @Override
    public Student getStudentById(String stuId) {
        Transaction transaction = null;
        Student student = null;

        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            transaction = session.beginTransaction();

            Query<Student> query = session.createQuery("FROM Student u WHERE u.id = :id", Student.class);
            query.setParameter("id", stuId);
            student = query.uniqueResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }

        return student;
    }

    @Override
    public List<Registration> getAllStudentWithCourse() throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        List<Registration> registrations = session.createQuery("from Registration").list();
        for (Registration registration : registrations) {
            System.out.println("Registration ID: " + registration.getId() + ", Student: " + registration.getStudent().getName() + ", Program: " + registration.getProgram().getProgramName());
        }
        transaction.commit();
        session.close();
        return registrations;
    }


}
