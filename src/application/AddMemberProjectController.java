package application;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddMemberProjectController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField roleInProjectField;
    @FXML
    private TextField involvementLevelField;
    @FXML
    private TextField feedbackField;

    private DatabaseConnection data = new DatabaseConnection();
    private int projectId; 
    private boolean saveClicked = false;

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        String name = nameField.getText().trim();
        String roleInProject = roleInProjectField.getText().trim();
        int involvementLevel = Integer.parseInt(involvementLevelField.getText().trim());
        String feedback = feedbackField.getText().trim();

        if (name.isEmpty() || roleInProject.isEmpty() ) {
            
            return;
        }

        try (Connection conn = data.getConnection()) {
            
            String ssnQuery = "SELECT ssn FROM \"IMPACT Club\".person WHERE first_name || ' ' || middle_name || ' ' || last_name = ?";
            PreparedStatement ssnStmt = conn.prepareStatement(ssnQuery);
            ssnStmt.setString(1, name);
            ResultSet ssnResult = ssnStmt.executeQuery();

            if (ssnResult.next()) {
                String ssn = ssnResult.getString("ssn");

              
                String memberIdQuery = "SELECT memberid FROM \"IMPACT Club\".member WHERE ssn = ?";
                PreparedStatement memberIdStmt = conn.prepareStatement(memberIdQuery);
                memberIdStmt.setString(1, ssn);
                ResultSet memberIdResult = memberIdStmt.executeQuery();

                if (memberIdResult.next()) {
                    int memberId = memberIdResult.getInt("memberid");

                   
                    String insertQuery = "INSERT INTO \"IMPACT Club\".memberproject (memberid, projectid, roleinproject, projectinvolvementlevel, projectfeedback) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                    insertStmt.setInt(1, memberId);
                    insertStmt.setInt(2, projectId);
                    insertStmt.setString(3, roleInProject);
                    insertStmt.setInt(4, involvementLevel);
                    insertStmt.setString(5, feedback);

                    insertStmt.executeUpdate();
                    saveClicked = true;

                   
                    Stage stage = (Stage) nameField.getScene().getWindow();
                    stage.close();
                } else {
                    System.out.println("Member ID not found for SSN: " + ssn);
                }
            } else {
                System.out.println("SSN not found for name: " + name);
            }
        } catch (SQLException ex) {
           
        }
    }

    @FXML
    private void handleCancel() {
        
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}