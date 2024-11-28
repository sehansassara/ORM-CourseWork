package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.UserDTO;
import lk.ijse.entity.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface UserBo extends SuperBO {

    String generateUserId() throws SQLException, IOException, ClassNotFoundException;

    boolean save(UserDTO userDto) throws SQLException, ClassNotFoundException, IOException;

    UserDTO getUserByName(String userName);

    List<UserDTO> getAllUser() throws SQLException, ClassNotFoundException, IOException;

    UserDTO searchByCustomerId(String id) throws IOException;

    boolean update(UserDTO userDto) throws SQLException, ClassNotFoundException, IOException;

    boolean deleteUser(UserDTO userDTO) throws SQLException, ClassNotFoundException, IOException;

    User getUserById(String uId);

    boolean updatePass(String newPassword, String userName);

    int getUserCount();
}
