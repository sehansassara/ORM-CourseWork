package lk.ijse.dto;

import lk.ijse.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDTO {
    private String pay_id;
    private String pay_date;
    private double balance_amount;
    private double pay_amount;
    private double upfront_amount;
    private Student student;
}
