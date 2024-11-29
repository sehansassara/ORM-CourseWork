package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.dto.ProgramDTO;
import lk.ijse.entity.Program;

import java.io.IOException;

public interface ProgramDAO extends CrudDAO<Program> {
    ProgramDTO getProgramDetailsByName(String selectedProgramId) throws IOException;

    boolean deleteAll(String id) throws IOException;
}
