package lk.ijse.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<T> extends SuperDAO{
    public String generateId() throws SQLException, ClassNotFoundException, IOException;

    public List<T> getAll() throws SQLException, ClassNotFoundException, IOException;

    public boolean delete(T batchDTO) throws SQLException, ClassNotFoundException, IOException;

    public boolean save(T batchDTO) throws SQLException, ClassNotFoundException, IOException;

    public boolean update(T batchDTO) throws SQLException, ClassNotFoundException, IOException;

    T searchById(String id) throws IOException;
}
