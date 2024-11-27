package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.dto.RegistrationDTO;
import lk.ijse.entity.Registration;
import lk.ijse.entity.Student;

import java.io.IOException;
import java.util.List;

public interface StudentDAO extends CrudDAO<Student> {
    List<Student> getStudentsRegisteredForAllCulinaryPrograms() throws IOException;

    Student getStudentById(String stuId);

    List<Registration> getAllStudentWithCourse() throws IOException;


    /* List<Object[]> getStudentsWithCoursesNative();*/
}
