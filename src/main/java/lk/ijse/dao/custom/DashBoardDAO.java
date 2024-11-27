package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;

import java.io.IOException;
import java.util.Map;

public interface DashBoardDAO {
    Map<String, Double> getPaymentsByDay() throws IOException;

    Map<String, Long> getStudentCountByCourse() throws IOException;

    int getStudentCount() throws IOException;

    int getProgramCount() throws IOException;

    int getPaymentCount() throws IOException;
}
