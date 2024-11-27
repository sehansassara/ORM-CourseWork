package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.RegistrationDTO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.entity.Program;
import lk.ijse.entity.Student;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface RegistrationBo extends SuperBO {
    String generateRegId();

    List<String> getIds();

    Student getStudentById(String stuId) throws IOException;

    Program getProgramDetailsById(String proId) throws IOException;

    boolean saveregisterdStudent(RegistrationDTO registrationDTO) throws SQLException, IOException, ClassNotFoundException;

    StudentDTO searchByStudentId(String id) throws IOException;

    List<RegistrationDTO> getAllStudentCourse();
}
