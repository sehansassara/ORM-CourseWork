package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.RegistrationBo;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.RegistrationDAO;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.dto.RegistrationDTO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.entity.Program;
import lk.ijse.entity.Registration;
import lk.ijse.entity.Student;
import lk.ijse.entity.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RegistrationBOImpl implements RegistrationBo {
    StudentDAO studentDAO = (StudentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.STUDENT);
    RegistrationDAO registrationDAO = (RegistrationDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.REGISTRATION);
    @Override
    public String generateRegId() {
        try {
            return registrationDAO.generateId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getIds() {
        try {
            return registrationDAO.getProgramIds();
        } catch (IOException e) {
            throw new RuntimeException("Error fetching program IDs", e);
        }
    }

    @Override
    public Student getStudentById(String stuId) throws IOException {
        Student student = registrationDAO.getUserById(stuId);

        return new Student(
                student.getId(),
                student.getUser(),
                student.getName(),
                student.getEmail(),
                student.getPhoneNumber(),
                student.getAddress()
        );
    }

    @Override
    public Program getProgramDetailsById(String proId) throws IOException {
        Program program = registrationDAO.getProgramById(proId);

        return new Program(
                program.getId(),
                program.getProgramName(),
                program.getDuration(),
                program.getFee()
        );
    }

    @Override
    public boolean saveregisterdStudent(RegistrationDTO registrationDTO) throws SQLException, IOException, ClassNotFoundException {
        return registrationDAO.save(new Registration(registrationDTO.getId(), registrationDTO.getStudentId(), registrationDTO.getProgramId(),registrationDTO.getDate(),registrationDTO.getPaymentAmount()));
    }

    @Override
    public StudentDTO searchByStudentId(String id) throws IOException {
        Student student = studentDAO.searchById(id);
        return new StudentDTO(student.getId(),student.getUser(),student.getName(),student.getEmail(),student.getPhoneNumber(),student.getAddress());
    }
}
