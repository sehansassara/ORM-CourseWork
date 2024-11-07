package lk.ijse.tm;

import com.jfoenix.controls.JFXButton;
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
    private JFXButton remove;

}