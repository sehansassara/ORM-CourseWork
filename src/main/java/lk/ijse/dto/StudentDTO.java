package lk.ijse.dto;

import lk.ijse.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class StudentDTO {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private User user;

    public StudentDTO(String id, User user, String name, String email, String phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.user = user;
    }

    public StudentDTO(String id, String name, String phoneNumber, String email, String address, User user) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.user = user;
    }

    public StudentDTO(String studentId, String studentName, String studentEmail, String studentPhone, String studentAddress, String courseName) {
        this.id = studentId;
        this.name = studentName;
        this.email = studentEmail;
        this.phoneNumber = studentPhone;
        this.address = studentAddress;
        this.user = new User(studentId, studentName, studentEmail, studentPhone, studentAddress, courseName);
        this.enrolledCourses = courseName;
    }
}
