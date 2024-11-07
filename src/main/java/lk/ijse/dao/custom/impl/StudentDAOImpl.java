package lk.ijse.dao.custom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lk.ijse.comfit.FactoryConfiguration;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.entity.Program;
import lk.ijse.entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.sql.SQLException;
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
   /* private EntityManager entityManager;

    @Override
    public List<Object[]> getStudentsWithCoursesNative() {
        // Define the native query to fetch students along with their enrolled courses
        String query = "SELECT s.id, s.name, s.email, s.phone_number, s.address, p.name AS course_name " +
                "FROM student s " +
                "JOIN registration r ON s.id = r.student_id " +
                "JOIN program p ON r.program_id = p.id";

        // Execute the native query and return the results as a list of Object[]
        Query nativeQuery = entityManager.createNativeQuery(query);

        // Return the result of the query execution
        return nativeQuery.getResultList();
    }*/
}
