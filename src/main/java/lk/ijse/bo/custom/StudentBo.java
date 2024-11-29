package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.RegistrationDTO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.entity.Student;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface StudentBo extends SuperBO {
    String generateStudentId();

    List<StudentDTO> getAllStudent();

    boolean saveStudent(StudentDTO studentDTO) throws SQLException, IOException, ClassNotFoundException;

    boolean updateStudent(StudentDTO student) throws SQLException, IOException, ClassNotFoundException;

    boolean deleteStudent(StudentDTO student) throws SQLException, IOException, ClassNotFoundException;

    StudentDTO searchByStudentId(String id) throws IOException;

    List<StudentDTO> getStudentsRegisteredForAllCulinaryPrograms() throws IOException;

    Student getStudentById(String stuId);

    boolean deleteStudentAll(String id) throws IOException;



    /*List<StudentDTO> getStudentsWithCourses();*/
}
