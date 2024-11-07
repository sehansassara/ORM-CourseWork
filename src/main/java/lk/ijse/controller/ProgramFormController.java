package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.ProgramBo;
import lk.ijse.dto.ProgramDTO;
import lk.ijse.tm.ProgramTm;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class ProgramFormController {

    @FXML
    private AnchorPane anchorProgram;

    @FXML
    private TableColumn<?, ?> colDuration;

    @FXML
    private TableColumn<?, ?> colFee;

    @FXML
    private TableColumn<?, ?> colProgramId;

    @FXML
    private TableColumn<?, ?> colProgramName;

    @FXML
    private TableView<ProgramTm> tblProgram;

    @FXML
    private TextField txtDuration;

    @FXML
    private TextField txtFee;

    @FXML
    private TextField txtProgramId;

    @FXML
    private TextField txtProgramName;

    ProgramBo programBo = (ProgramBo) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PROGRAM);

    public void initialize() throws SQLException, IOException, ClassNotFoundException {
        loadAllPrograms();
        setCellValueFactory();
        generateNextProgramId();
        clearFields();
    }

    private void clearFields() {
        txtDuration.setText("");
        txtFee.setText("");
        txtProgramName.setText("");
    }

    private String generateNextProgramId() throws SQLException, IOException, ClassNotFoundException {
        String id = programBo.generateProgramId();
        txtProgramId.setText(id);
        return id;
    }

    private void setCellValueFactory() {
        colProgramId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProgramName.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
    }

    private void loadAllPrograms() {
        ObservableList<ProgramTm> obList = FXCollections.observableArrayList();
        List<ProgramDTO> programDTOList;
        try {
            programDTOList = programBo.getAllUser();
            for (ProgramDTO programDTO : programDTOList) {
                ProgramTm programTm = new ProgramTm(programDTO.getId(), programDTO.getProgramName(), programDTO.getDuration(), programDTO.getFee());
                obList.add(programTm);
            }
            tblProgram.setItems(obList);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading programs: " + e.getMessage()).show();
        }
    }

    private boolean validateFields() {
        String programName = txtProgramName.getText();
        String duration = txtDuration.getText();
        String fee = txtFee.getText();

        if (programName.isEmpty() || !Pattern.matches("^[A-Za-z0-9\\s&'-]+$", programName)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Program Name!").show();
            return false;
        }

        if (duration.isEmpty() || !Pattern.matches("^[0-9]+\\s+(months?|weeks?|days?)$", duration)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Duration! Use format like '3 months', '2 weeks'").show();
            return false;
        }

        if (fee.isEmpty() || !Pattern.matches("^\\d+(\\.\\d{1,2})?$", fee)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Fee! Use a valid number (e.g., 1500.00)").show();
            return false;
        }

        return true;
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        if (!validateFields()) return;

        String id = txtProgramId.getText();
        String programName = txtProgramName.getText();
        String duration = txtDuration.getText();
        double fee = Double.parseDouble(txtFee.getText());

        ProgramDTO programDTO = new ProgramDTO(id, programName, duration, fee);
        if (programBo.deleteProgram(programDTO)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Program Deleted Successfully!").show();
            generateNextProgramId();
            clearFields();
            loadAllPrograms();
        } else {
            new Alert(Alert.AlertType.ERROR, "SQL Error").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        if (!validateFields()) return;

        String id = txtProgramId.getText();
        String programName = txtProgramName.getText();
        String duration = txtDuration.getText();
        double fee = Double.parseDouble(txtFee.getText());

        ProgramDTO programDTO = new ProgramDTO(id, programName, duration, fee);
        if (programBo.saveProgram(programDTO)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Program Added Successfully!").show();
            generateNextProgramId();
            clearFields();
            loadAllPrograms();
        } else {
            new Alert(Alert.AlertType.ERROR, "SQL Error").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        if (!validateFields()) return;

        String id = txtProgramId.getText();
        String programName = txtProgramName.getText();
        String duration = txtDuration.getText();
        double fee = Double.parseDouble(txtFee.getText());

        ProgramDTO programDTO = new ProgramDTO(id, programName, duration, fee);
        if (programBo.updateProgram(programDTO)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Program Updated Successfully!").show();
            generateNextProgramId();
            clearFields();
            loadAllPrograms();
        } else {
            new Alert(Alert.AlertType.ERROR, "SQL Error").show();
        }
    }

    @FXML
    void tblProgramOnMouse(MouseEvent event) {
        int index = tblProgram.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }

        String id = colProgramId.getCellData(index).toString();
        String name = colProgramName.getCellData(index).toString();
        String duration = colDuration.getCellData(index).toString();
        String fee = colFee.getCellData(index).toString();

        txtProgramId.setText(id);
        txtProgramName.setText(name);
        txtDuration.setText(duration);
        txtFee.setText(fee);
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) throws IOException {
        try {
            String id = txtProgramId.getText();
            ProgramDTO programDTO = programBo.searchByProgramId(id);

            if (programDTO != null) {
                txtProgramId.setText(programDTO.getId());
                txtProgramName.setText(programDTO.getProgramName());
                txtDuration.setText(programDTO.getDuration());
                txtFee.setText(String.valueOf(programDTO.getFee()));
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Program not found!").show();
            }
        } catch (NullPointerException e) {
            new Alert(Alert.AlertType.ERROR, "Program not found!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Program not found!").show();
            e.printStackTrace();
        }
    }

}
