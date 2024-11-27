package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.StudentBo;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.ProgramDAO;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.dto.ProgramDTO;
import lk.ijse.dto.RegistrationDTO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.entity.Program;
import lk.ijse.entity.Student;
import lk.ijse.entity.User;

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

    @Override
    public List<StudentDTO> getStudentsRegisteredForAllCulinaryPrograms() {
        List<StudentDTO> studentDTOS = new ArrayList<>();
        List<Student> students;

        try {
            students = studentDAO.getStudentsRegisteredForAllCulinaryPrograms();

            // Debugging output
            System.out.println("Number of DTOs to convert: " + students.size());

            for (Student student : students) {
                studentDTOS.add(new StudentDTO(student.getId(), student.getName(), student.getEmail(),
                        student.getPhoneNumber(), student.getAddress(), student.getUser()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error retrieving students registered for all culinary programs", e);
        }

        return studentDTOS;
    }

    @Override
    public Student getStudentById(String stuId) {
        Student student = studentDAO.getStudentById(stuId);

        return new Student(
                student.getId(),
                student.getUser(),
                student.getName(),
                student.getEmail(),
                student.getPhoneNumber(),
                student.getAddress()
        );
    }



}
