package lk.ijse.bo;

import lk.ijse.bo.custom.impl.*;
import lk.ijse.entity.Registration;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {}

    public static BOFactory getBoFactory() {
        return (boFactory==null) ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes{
        USER,PROGRAM,STUDENT,REGISTRATION,PAYMENT
    }

    public SuperBO getBO(BOTypes types) {
        switch (types) {
            case USER:
                return new UserBOImpl();
            case PROGRAM:
                return new ProgramBOImpl();
            case STUDENT:
                return new StudentBOImpl();
                case REGISTRATION:
                    return new RegistrationBOImpl();
                 case PAYMENT:
                     return new PaymentBOImpl();
            default:
                return null;
        }
    }
}
