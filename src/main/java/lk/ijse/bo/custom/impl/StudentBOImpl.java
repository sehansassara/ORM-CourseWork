package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.StudentBo;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.ProgramDAO;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.dto.ProgramDTO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.entity.Program;
import lk.ijse.entity.Student;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentBOImpl implements StudentBo {
    StudentDAO studentDAO = (StudentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.STUDENT);

    @Override
    public String generateStudentId() {
        try {
            return studentDAO.generateId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<StudentDTO> getAllStudent() {
        List<StudentDTO> studentDTOS = new ArrayList<>();
        List<Student> students = null;
        try {
            students = studentDAO.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Student student : students) {
            studentDTOS.add(new StudentDTO(student.getId(),student.getName(),student.getEmail(), student.getPhoneNumber(),student.getAddress(),student.getUser()));
        }
        return studentDTOS;
    }

    @Override
    public boolean saveStudent(StudentDTO studentDTO) throws SQLException, IOException, ClassNotFoundException {
        return studentDAO.save(new Student(studentDTO.getId(),studentDTO.getUser(),studentDTO.getName(),studentDTO.getEmail(),studentDTO.getPhoneNumber(),studentDTO.getAddress()));
    }

    @Override
    public boolean updateStudent(StudentDTO studentDTO) throws SQLException, IOException, ClassNotFoundException {
        return studentDAO.update(new Student(studentDTO.getId(),studentDTO.getUser(),studentDTO.getName(),studentDTO.getPhoneNumber(),studentDTO.getEmail(),studentDTO.getAddress()));
    }

    @Override
    public boolean deleteStudent(StudentDTO studentDTO) throws SQLException, IOException, ClassNotFoundException {
        return studentDAO.delete(new Student(studentDTO.getId(),studentDTO.getUser(),studentDTO.getName(),studentDTO.getEmail(),studentDTO.getPhoneNumber(),studentDTO.getAddress()));

    }

    @Override
    public StudentDTO searchByStudentId(String id) throws IOException {
        Student student = studentDAO.searchById(id);
        return new StudentDTO(student.getId(),student.getUser(),student.getName(),student.getEmail(),student.getPhoneNumber(),student.getAddress());
    }

   /* @Override
    public List<StudentDTO> getStudentsWithCourses() {
            // Call the repository method to fetch the result from the database
            List<Object[]> result = studentDAO.getStudentsWithCoursesNative();

            // Create an empty list to hold the mapped StudentDTOs
            List<StudentDTO> studentDTOList = new ArrayList<>();

            // Process each row in the result and map it to a StudentDTO
            for (Object[] row : result) {
                // Map the result to individual fields
                String studentId = (String) row[0];       // student ID
                String studentName = (String) row[1];     // student name
                String studentEmail = (String) row[2];    // student email
                String studentPhone = (String) row[3];    // student phone number
                String studentAddress = (String) row[4];  // student address
                String courseName = (String) row[5];      // course name (program name)

                // Create a new StudentDTO object and populate it
                StudentDTO studentDTO = new StudentDTO(studentId, studentName, studentEmail, studentPhone, studentAddress, courseName);

                // Add the created DTO to the list
                studentDTOList.add(studentDTO);
            }

            // Return the list of StudentDTOs
            return studentDTOList;
        }*/
}
