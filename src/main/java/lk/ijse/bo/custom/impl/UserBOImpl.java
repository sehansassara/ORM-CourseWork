package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.UserBo;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.UserDAO;
import lk.ijse.dto.UserDTO;
import lk.ijse.entity.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserBOImpl implements UserBo {
    UserDAO userDAO = (UserDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.USER);

    @Override
    public String generateUserId() throws SQLException, IOException, ClassNotFoundException {
            return userDAO.generateId();

    }

    @Override
    public boolean save(UserDTO userDto) throws SQLException, ClassNotFoundException, IOException {
        return userDAO.save(new User(userDto.getUser_id(),userDto.getUser_name(),userDto.getUser_email(),userDto.getUser_tel(),userDto.getPassword(),userDto.getUser_role()));
    }

    @Override
    public UserDTO getUserByName(String userName) {
        User user = userDAO.getUserByName(userName);
        if (user == null) {
            return null;
        }
            return new UserDTO(
                    user.getUser_id(),
                    user.getUser_name(),
                    user.getUser_email(),
                    user.getUser_tel(),
                    user.getPassword(),
                    user.getUser_role()
            );
    }

    @Override
    public List<UserDTO> getAllUser() throws SQLException, ClassNotFoundException, IOException {
        List<UserDTO> userDTOS = new ArrayList<>();
        List<User> users = userDAO.getAll();
        for (User user : users) {
            userDTOS.add(new UserDTO(user.getUser_id(),user.getUser_name(),user.getUser_email(),user.getUser_tel(),user.getPassword(),user.getUser_role()));
        }
        return userDTOS;
    }

    @Override
    public UserDTO searchByCustomerId(String id) throws IOException {
        User user = userDAO.searchById(id);
        return new UserDTO(user.getUser_id(),user.getUser_name(),user.getUser_email(),user.getUser_tel(),user.getPassword(),user.getUser_role());
    }

    @Override
    public boolean update(UserDTO userDto) throws SQLException, ClassNotFoundException, IOException {
        return userDAO.update(new User(userDto.getUser_id(),userDto.getUser_name(),userDto.getUser_email(),userDto.getUser_tel(),userDto.getPassword(),userDto.getUser_role()));
    }

    @Override
    public boolean deleteUser(UserDTO userDTO) throws SQLException, ClassNotFoundException, IOException {
        return userDAO.delete(new User(userDTO.getUser_id(),userDTO.getUser_name(),userDTO.getUser_email(),userDTO.getUser_tel(),userDTO.getPassword(),userDTO.getUser_role()));
    }

    @Override
    public User getUserById(String uId) {
        User user = userDAO.getUserById(uId);

        return new User(
                user.getUser_id(),
                user.getUser_name(),
                user.getUser_email(),
                user.getUser_tel(),
                user.getPassword(),
                user.getUser_role()
        );
    }

    @Override
    public boolean updatePass(String newPassword, String userName) {
        return userDAO.updatePass(newPassword,userName);
    }

    @Override
    public int getUserCount() {
        return userDAO.getUserCount();
    }
}
