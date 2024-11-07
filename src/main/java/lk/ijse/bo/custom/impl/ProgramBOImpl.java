package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.ProgramBo;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.ProgramDAO;
import lk.ijse.dao.custom.UserDAO;
import lk.ijse.dto.ProgramDTO;
import lk.ijse.dto.UserDTO;
import lk.ijse.entity.Program;
import lk.ijse.entity.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProgramBOImpl implements ProgramBo {
    ProgramDAO programDAO = (ProgramDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PROGRAM);

    @Override
    public boolean updateProgram(ProgramDTO programDTO) throws SQLException, IOException, ClassNotFoundException {
        return programDAO.update(new Program(programDTO.getId(),programDTO.getProgramName(),programDTO.getDuration(),programDTO.getFee()));
    }

    @Override
    public boolean deleteProgram(ProgramDTO programDTO) throws SQLException, IOException, ClassNotFoundException {
        return programDAO.delete(new Program(programDTO.getId(),programDTO.getProgramName(),programDTO.getDuration(),programDTO.getFee()));

    }

    @Override
    public ProgramDTO searchByProgramId(String id) throws IOException {
        Program program = programDAO.searchById(id);
        return new ProgramDTO(program.getId(),program.getProgramName(),program.getDuration(),program.getFee());

    }

    @Override
    public ProgramDTO getProgramDetailsByName(String selectedProgramId) throws IOException {
        return programDAO.getProgramDetailsByName(selectedProgramId);

    }

    @Override
    public String generateProgramId() throws SQLException, IOException, ClassNotFoundException {
        return programDAO.generateId();
    }

    @Override
    public List<ProgramDTO> getAllUser() throws SQLException, IOException, ClassNotFoundException {
        List<ProgramDTO> programDTOS = new ArrayList<>();
        List<Program> programs = programDAO.getAll();
        for (Program program : programs) {
            programDTOS.add(new ProgramDTO(program.getId(),program.getProgramName(),program.getDuration(),program.getFee()));
        }
        return programDTOS;
    }

    @Override
    public boolean saveProgram(ProgramDTO programDTO) throws SQLException, IOException, ClassNotFoundException {
        return programDAO.save(new Program(programDTO.getId(),programDTO.getProgramName(),programDTO.getDuration(),programDTO.getFee()));
    }
}
