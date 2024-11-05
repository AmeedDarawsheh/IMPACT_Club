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
    private int projectId; // Project ID set by another method or selected by the user

    // Method to initialize columns and set up TableView
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        leaderNameColumn.setCellValueFactory(new PropertyValueFactory<>("leaderName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        feedbackColumn.setCellValueFactory(new PropertyValueFactory<>("feedback"));

        // Call this method when the projectId is set or changed
        loadProjectLeaders();
    }

    // Method to load project leaders data
    private void loadProjectLeaders() {
        leaderData.clear(); // Clear any existing data
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

                // Add new ProjectLeader object to the observable list
                leaderData.add(new ProjectLeader(id, leaderName, role, feedback));
            }
            tableView.setItems(leaderData); // Set data to TableView
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to set the project ID (e.g., when a project is chosen by the user)
    public void setProjectId(int projectId) {
        this.projectId = projectId;
        loadProjectLeaders(); // Load data for the chosen project
    }
    @FXML
    private void onGiveFeedbackClick() {
        // Assuming you have a way to get the logged-in leader ID
        int loggedInLeaderId = loginController.getLoggedInLeaderId(); // Replace with your logic to retrieve the logged-in leader's ID
        System.out.print(loggedInLeaderId);
        // Get the selected leader from the table view
        ProjectLeader selectedLeader = tableView.getSelectionModel().getSelectedItem();
        
        if (selectedLeader == null) {
            showAlert("No Leader Selected", "Please select a leader to give feedback.");
            return;
        }

        // Check if the selected leader's ID matches the logged-in leader's ID
        if (selectedLeader.getId() != loggedInLeaderId) {
            showAlert("Access Denied", "You can only edit your own feedback.");
            return;
        }

        // Show input dialog for feedback
        TextInputDialog feedbackDialog = new TextInputDialog(selectedLeader.getFeedback());
        feedbackDialog.setTitle("Edit Feedback");
        feedbackDialog.setHeaderText("Edit your feedback for this project:");
        feedbackDialog.setContentText("Feedback:");

        Optional<String> result = feedbackDialog.showAndWait();
        result.ifPresent(feedback -> {
            // Update feedback in the database
            if (updateFeedbackInDatabase(loggedInLeaderId, feedback)) {
                selectedLeader.setFeedback(feedback); // Update local data
                tableView.refresh(); // Refresh table to show updated feedback
                showAlert("Success", "Feedback updated successfully.");
            } else {
                showAlert("Error", "Failed to update feedback. Please try again.");
            }
        });
    }

    // Method to show an alert dialog
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Method to update feedback in the database
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
        // Assuming you have a way to get the logged-in leader ID
        int loggedInLeaderId = loginController.getLoggedInLeaderId(); // Replace with your logic to retrieve the logged-in leader's ID
        System.out.print(loggedInLeaderId);
        // Get the selected leader from the table view
        ProjectLeader selectedLeader = tableView.getSelectionModel().getSelectedItem();
        
        if (selectedLeader == null) {
            showAlert("No Leader Selected", "Please select a leader to give feedback.");
            return;
        }

        // Check if the selected leader's ID matches the logged-in leader's ID
        if (selectedLeader.getId() != loggedInLeaderId) {
            showAlert("Access Denied", "You can only edit your own role.");
            return;
        }

        // Show input dialog for feedback
        TextInputDialog RoleDialog = new TextInputDialog(selectedLeader.getRole());
        RoleDialog.setTitle("Edit Role");
        RoleDialog.setHeaderText("Edit your Role for this project:");
        RoleDialog.setContentText("Role:");

        Optional<String> result = RoleDialog.showAndWait();
        result.ifPresent(Role -> {
            // Update feedback in the database
            if (updateRoleInDatabase(loggedInLeaderId, Role)) {
                selectedLeader.setRole(Role); // Update local data
                tableView.refresh(); // Refresh table to show updated feedback
                showAlert("Success", "Role updated successfully.");
            } else {
                showAlert("Error", "Failed to update Role. Please try again.");
            }
        });
    }

    
    // Method to update feedback in the database
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
