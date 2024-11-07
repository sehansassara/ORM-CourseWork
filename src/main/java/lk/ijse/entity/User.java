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
public class User {
    @Id
    private String user_id;
    private String user_name;
    private String user_email;
    private String user_tel;
    private String password;
    private String user_role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;

    public User(String userId, String userName, String userEmail, String userTel, String password, String userRole) {
        this.user_id = userId;
        this.user_name = userName;
        this.user_email = userEmail;
        this.user_tel = userTel;
        this.password = password;
        this.user_role = userRole;
    }
}
