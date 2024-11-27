package lk.ijse.dao;

import lk.ijse.dao.custom.impl.*;
import lk.ijse.entity.Registration;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {}

    public static DAOFactory getDaoFactory() {
        return (daoFactory==null) ? daoFactory = new DAOFactory() : daoFactory;
    }

    public enum DAOTypes{
        USER,PROGRAM,STUDENT,REGISTRATION,PAYMENTS
    }

    public SuperDAO getDAO(DAOTypes types) {
        switch (types) {
            case USER:
                return new UserDAOImpl();
            case PROGRAM:
                return new ProgramDAOImpl();
            case STUDENT:
                return new StudentDAOImpl();
                case REGISTRATION:
                    return new RegistrationDAOImpl();
                    case PAYMENTS:
                        return new PaymentDAOImpl();
            default:
                return null;
        }
    }
}
