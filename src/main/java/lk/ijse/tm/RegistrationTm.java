package lk.ijse.tm;

import com.jfoenix.controls.JFXButton;
import lk.ijse.entity.Program;
import lk.ijse.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistrationTm {
    private String regId;
    private String date;
    private String stuId;
    private String couId;
    private double fee;

    public RegistrationTm(String id, String date, Student studentId, Program programId, double paymentAmount) {
    }
}