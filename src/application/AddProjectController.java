package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddProjectController {

    @FXML
    private TextField projectIdField, nameField, descriptionField, objectivesField, progressLevelField, planIdField;
    @FXML
    private RadioButton inProgressRadio, completedRadio;
    @FXML
    private DatePicker startDatePicker, endDatePicker;
    @FXML
    private Button saveButton, cancelButton;
    DatabaseConnection data=new DatabaseConnection();
    private Connection connection=data.getConnection(); 

    public void initialize() {
        // Initialize toggle groups for radio buttons
        ToggleGroup statusGroup = new ToggleGroup();
        inProgressRadio.setToggleGroup(statusGroup);
        completedRadio.setToggleGroup(statusGroup);
    }

    @FXML
    public void handleSaveButton(ActionEvent event) {
        // Gather values from fields
        String projectId = projectIdField.getText();
        String name = nameField.getText();
        String description = descriptionField.getText();
        String objectives = objectivesField.getText();
        String progressLevel = progressLevelField.getText();
        String planId = planIdField.getText();
        String status = inProgressRadio.isSelected() ? "In Progress" : "Completed";
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        // Validate inputs
        if (projectId.isEmpty() || name.isEmpty() || description.isEmpty() || objectives.isEmpty() ||
                progressLevel.isEmpty() || planId.isEmpty() || startDate == null || endDate == null) {
            showAlert("All fields are required.");
            return;
        }

        try {
            // Start transaction
            connection.setAutoCommit(false);

          
            if (!isPlanIdExists(Integer.parseInt(planId))) {
                
                String insertPlanQuery = "INSERT INTO \"IMPACT Club\".plan (planid, startdate, enddate) VALUES (?, ?, ?)";
                try (PreparedStatement psPlan = connection.prepareStatement(insertPlanQuery)) {
                    psPlan.setInt(1, Integer.parseInt(planId));
                    psPlan.setDate(2, java.sql.Date.valueOf(startDate));
                    psPlan.setDate(3, java.sql.Date.valueOf(endDate));
                    psPlan.executeUpdate();
                }
            }

            
            String projectQuery = "INSERT INTO \"IMPACT Club\".project (projectid, projectname, description, projectstatus, objectives) " +
                                  "VALUES (?, ?, ?, ?::\"IMPACT Club\".projectstatustype, ?)";
            try (PreparedStatement psProject = connection.prepareStatement(projectQuery)) {
                psProject.setInt(1, Integer.parseInt(projectId));
                psProject.setString(2, name);
                psProject.setString(3, description);
                psProject.setString(4, status); // Enum status
                psProject.setString(5, objectives);
                psProject.executeUpdate();
            }

           
            String projectPlanQuery = "INSERT INTO \"IMPACT Club\".projectplan (planid, projectid, budget, expenditure, fundingsources, objectives) " +
                                      "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psProjectPlan = connection.prepareStatement(projectPlanQuery)) {
                psProjectPlan.setInt(1, Integer.parseInt(planId)); 
                psProjectPlan.setInt(2, Integer.parseInt(projectId));
                psProjectPlan.setDouble(3, 0.0); 
                psProjectPlan.setDouble(4, 0.0); 
                psProjectPlan.setString(5, "Example Funding Sources"); 
                psProjectPlan.setString(6, objectives);
                psProjectPlan.executeUpdate();
            }

           
            connection.commit();
            showAlert("Project and Project Plan added successfully.");

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
               
            }
            
            showAlert("An error occurred while adding the project.");
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                
            }
        }
    }

    
    private boolean isPlanIdExists(int planId) throws SQLException {
        String query = "SELECT 1 FROM \"IMPACT Club\".plan WHERE planid = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, planId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleCancelButton(ActionEvent event) {
       
        projectIdField.clear();
        nameField.clear();
        descriptionField.clear();
        objectivesField.clear();
        progressLevelField.clear();
        planIdField.clear();
        inProgressRadio.setSelected(true);
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
    }
}
