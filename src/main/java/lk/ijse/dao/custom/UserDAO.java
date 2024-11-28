package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.entity.User;

public interface UserDAO extends CrudDAO<User> {
    User getUserByName(String userName);

    User getUserById(String userId);

    boolean updatePass(String newPassword, String userName);

    int getUserCount();
}
