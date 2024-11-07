package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.ProgramDTO;
import lk.ijse.entity.Program;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface ProgramBo extends SuperBO {
    String generateProgramId() throws SQLException, IOException, ClassNotFoundException;

    List<ProgramDTO> getAllUser() throws SQLException, IOException, ClassNotFoundException;

    boolean saveProgram(ProgramDTO programDTO) throws SQLException, IOException, ClassNotFoundException;

    boolean updateProgram(ProgramDTO programDTO) throws SQLException, IOException, ClassNotFoundException;

    boolean deleteProgram(ProgramDTO programDTO) throws SQLException, IOException, ClassNotFoundException;

    ProgramDTO searchByProgramId(String id) throws IOException;

    ProgramDTO getProgramDetailsByName(String selectedProgramId) throws IOException;

}
