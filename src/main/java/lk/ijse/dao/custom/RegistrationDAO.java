package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.entity.Program;
import lk.ijse.entity.Registration;
import lk.ijse.entity.Student;

import java.io.IOException;
import java.util.List;

public interface RegistrationDAO extends CrudDAO<Registration> {
    List<String> getProgramIds() throws IOException;

    Student getUserById(String stuId) throws IOException;

    Program getProgramById(String proId) throws IOException;

    Registration getRegById(String regId) throws IOException;

    boolean isStudentRegisteredForProgram(String stuId, String proId) throws IOException;
}
