package lk.ijse.dao.custom.impl;

import lk.ijse.comfit.FactoryConfiguration;
import lk.ijse.dao.custom.ProgramDAO;
import lk.ijse.dto.ProgramDTO;
import lk.ijse.entity.Program;
import lk.ijse.entity.Registration;
import lk.ijse.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProgramDAOImpl implements ProgramDAO {
    @Override
    public String generateId() throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        String nextId = "";
        try {
            String lastId = (String) session.createQuery("SELECT p.id FROM Program p ORDER BY p.id DESC")
                    .setMaxResults(1)
                    .uniqueResult();

            if (lastId != null) {
                String prefix = "CA";
                String[] split = lastId.split(prefix);

                int idNum = Integer.parseInt(split[1]);
                nextId = prefix + String.format("%04d", ++idNum);
            } else {
                nextId = "CA1001";
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
    public List<Program> getAll() throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        List<Program> programs = session.createQuery("from Program").list();
        transaction.commit();
        session.close();
        return programs;
    }

    @Override
    public boolean delete(Program batchDTO) throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.delete(batchDTO);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean save(Program batchDTO) throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.save(batchDTO);

        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Program batchDTO) throws SQLException, ClassNotFoundException, IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        session.update(batchDTO);

        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public Program searchById(String id) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        Object program = session.createQuery("SELECT p FROM Program p WHERE p.id = :id")
                .setParameter("id", id).uniqueResult();
        transaction.commit();
        session.close();
        return (Program) program;
    }

    @Override
    public ProgramDTO getProgramDetailsByName(String selectedProgramName) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        String hql = "FROM Program p WHERE p.programName = :programName";
        Query<Program> query = session.createQuery(hql, Program.class);
        query.setParameter("programName", selectedProgramName);

        Program program = (Program) query.uniqueResult();

        if (program != null) {
            ProgramDTO programDTO = new ProgramDTO(
                    program.getId(),
                    program.getProgramName(),
                    program.getDuration(),
                    program.getFee()
            );
            transaction.commit();
            return programDTO;
        } else {
            transaction.rollback();
            return null;
        }
    }

    @Override
    public boolean deleteAll(String programId) throws IOException {
        Session session = null;
        Transaction transaction = null;

        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();

            Program program = session.get(Program.class, programId);
            if (program == null) {
                return false;
            }

            for (Registration registration : program.getRegistrations()) {
                registration.setProgram(null);
                registration.setStudent(null);

                session.delete(registration);
            }

            program.getRegistrations().clear();

            session.delete(program);

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new IOException("Failed to delete program with ID: " + programId, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


}
