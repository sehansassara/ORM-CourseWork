package lk.ijse.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserTm {
    private String user_id;
    private String user_name;
    private String user_email;
    private String user_tel;
    private String password;
    private String user_role;

}