package lk.ijse.tm;

import lk.ijse.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentTm {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String user;
    private String couId;
    private String date;

    public StudentTm(String id, String user, String name, String email, String phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.user = user;
    }

    public StudentTm(String id, String userId, String name, String phoneNumber, String email, String address, String proId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.user = userId;
        this.couId = proId;
    }


}
