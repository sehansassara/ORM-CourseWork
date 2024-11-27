package lk.ijse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Payment {
    @Id
    private String pay_id;
    private String pay_date;
    private double balance_amount;
    private double pay_amount;
    private double upfront_amount;


    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}
