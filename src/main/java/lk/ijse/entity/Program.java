package lk.ijse.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Program {
    @Id
    private String id;
    private String programName;
    private String duration;
    private double fee;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Registration> registrations;

    public Program(String id, String programName, String duration, double fee) {
        this.id = id;
        this.programName = programName;
        this.duration = duration;
        this.fee = fee;
    }

    public Program(String s) {
        this.programName = s;
    }
}
