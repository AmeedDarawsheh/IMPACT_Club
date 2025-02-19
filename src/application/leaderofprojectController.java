package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class leaderofprojectController {

    @FXML
    private TableView<ProjectLeader> tableView;
    @FXML
    private TableColumn<ProjectLeader, Integer> idColumn;
    @FXML
    private TableColumn<ProjectLeader, String> leaderNameColumn;
    @FXML
    private TableColumn<ProjectLeader, String> roleColumn;
    @FXML
    private TableColumn<ProjectLeader, String> feedbackColumn;

    private ObservableList<ProjectLeader> leaderData = FXCollections.observableArrayList();
    private int projectId; 

 
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        leaderNameColumn.setCellValueFactory(new PropertyValueFactory<>("leaderName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        feedbackColumn.setCellValueFactory(new PropertyValueFactory<>("feedback"));

        loadProjectLeaders();
    }

   
    private void loadProjectLeaders() {
        leaderData.clear(); 
        String query = "SELECT l.leaderid AS idleader, " +
                       "p.first_name || ' ' || p.middle_name || ' ' || p.last_name AS leader_name, " +
                       "pl.role, pl.projectfeedback " +
                       "FROM \"IMPACT Club\".person p " +
                       "JOIN \"IMPACT Club\".leader l ON p.ssn = l.ssn " +
                       "JOIN \"IMPACT Club\".projectleader pl ON l.leaderid = pl.leaderid " +
                       "WHERE pl.projectid = ?;";

        try (Connection conn = new DatabaseConnection().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("idleader");
                String leaderName = rs.getString("leader_name");
                String role = rs.getString("role");
                String feedback = rs.getString("projectfeedback");

               
                leaderData.add(new ProjectLeader(id, leaderName, role, feedback));
            }
            tableView.setItems(leaderData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void setProjectId(int projectId) {
        this.projectId = projectId;
        loadProjectLeaders(); 
    }
    @FXML
    private void onGiveFeedbackClick() {
    
        int loggedInLeaderId = loginController.getLoggedInLeaderId(); 
        System.out.print(loggedInLeaderId);
     
        ProjectLeader selectedLeader = tableView.getSelectionModel().getSelectedItem();
        
        if (selectedLeader == null) {
            showAlert("No Leader Selected", "Please select a leader to give feedback.");
            return;
        }

        if (selectedLeader.getId() != loggedInLeaderId) {
            showAlert("Access Denied", "You can only edit your own feedback.");
            return;
        }


        TextInputDialog feedbackDialog = new TextInputDialog(selectedLeader.getFeedback());
        feedbackDialog.setTitle("Edit Feedback");
        feedbackDialog.setHeaderText("Edit your feedback for this project:");
        feedbackDialog.setContentText("Feedback:");

        Optional<String> result = feedbackDialog.showAndWait();
        result.ifPresent(feedback -> {
           
            if (updateFeedbackInDatabase(loggedInLeaderId, feedback)) {
                selectedLeader.setFeedback(feedback); 
                tableView.refresh(); 
                showAlert("Success", "Feedback updated successfully.");
            } else {
                showAlert("Error", "Failed to update feedback. Please try again.");
            }
        });
    }

   
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    
    private boolean updateFeedbackInDatabase(int leaderId, String feedback) {
        String query = "UPDATE \"IMPACT Club\".projectleader SET projectfeedback = ? WHERE leaderid = ?;";
        try (Connection conn = new DatabaseConnection().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, feedback);
            stmt.setInt(2, leaderId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @FXML
    private void onEditRoleClick() {
      
        int loggedInLeaderId = loginController.getLoggedInLeaderId();
        System.out.print(loggedInLeaderId);
        
        ProjectLeader selectedLeader = tableView.getSelectionModel().getSelectedItem();
        
        if (selectedLeader == null) {
            showAlert("No Leader Selected", "Please select a leader to give feedback.");
            return;
        }


        if (selectedLeader.getId() != loggedInLeaderId) {
            showAlert("Access Denied", "You can only edit your own role.");
            return;
        }

        
        TextInputDialog RoleDialog = new TextInputDialog(selectedLeader.getRole());
        RoleDialog.setTitle("Edit Role");
        RoleDialog.setHeaderText("Edit your Role for this project:");
        RoleDialog.setContentText("Role:");

        Optional<String> result = RoleDialog.showAndWait();
        result.ifPresent(Role -> {
        
            if (updateRoleInDatabase(loggedInLeaderId, Role)) {
                selectedLeader.setRole(Role); 
                tableView.refresh();
                showAlert("Success", "Role updated successfully.");
            } else {
                showAlert("Error", "Failed to update Role. Please try again.");
            }
        });
    }

    
   
    private boolean updateRoleInDatabase(int leaderId, String role) {
        String query = "UPDATE \"IMPACT Club\".projectleader SET role= ? WHERE leaderid = ?;";
        try (Connection conn = new DatabaseConnection().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, role);
            stmt.setInt(2, leaderId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

   
   
}
