package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class UserFeedBackController implements Initializable{

	
    @FXML
    private TextArea feedbackTextArea;
	@FXML
	TextArea text;
	String feedback;
	int memberId;
	int projectId,sessionId;
	boolean project =false;
	@FXML
	public void Send(ActionEvent e) {
	
		String feed = text.getText();
		
		// get user email  and send email to club  have a subject "feedback from user " and text 
		
		
	}
	@FXML
	public void cancel(ActionEvent e) {
		
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		// fill User Projects Done Only
		
	}
	public void setFeedback(int memberId, int pId,boolean isproject) {
		 this.memberId = memberId;
		if(isproject) {
			project=true;
       
        this.projectId = pId;}
		
		else {sessionId=pId;}
    }

    @FXML
    private void submitFeedback() {
    	if(project) {
        String feedback = feedbackTextArea.getText();

        // SQL to update feedback in the database
        String query = """
            UPDATE "IMPACT Club".memberproject
            SET projectfeedback = ?
            WHERE memberid = ? AND projectid = ?;
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, feedback);
            stmt.setInt(2, memberId);
            stmt.setInt(3, projectId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Show success alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Feedback Submitted");
                alert.setHeaderText(null);
                alert.setContentText("Your feedback has been saved successfully.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Show error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to save feedback. Please try again.");
            alert.showAndWait();
        }
    }
    else {
    	 String query = """
    	            UPDATE "IMPACT Club".sessionmember
    	            SET feedback = ?
    	            WHERE memberid = ? AND sessionid = ?;
    	            """;
    	 String feedback = feedbackTextArea.getText();
    	        try (Connection conn = DatabaseConnection.getConnection();
    	             PreparedStatement stmt = conn.prepareStatement(query)) {

    	            stmt.setString(1, feedback);
    	            stmt.setInt(2, memberId);
    	            stmt.setInt(3, sessionId);

    	            int rowsAffected = stmt.executeUpdate();
    	            if (rowsAffected > 0) {
    	                // Show success alert
    	                Alert alert = new Alert(Alert.AlertType.INFORMATION);
    	                alert.setTitle("Feedback Submitted");
    	                alert.setHeaderText(null);
    	                alert.setContentText("Your feedback has been saved successfully.");
    	                alert.showAndWait();
    	            }
    	        } catch (SQLException e) {
    	            e.printStackTrace();
    	            // Show error alert
    	            Alert alert = new Alert(Alert.AlertType.ERROR);
    	            alert.setTitle("Database Error");
    	            alert.setHeaderText(null);
    	            alert.setContentText("Failed to save feedback. Please try again.");
    	            alert.showAndWait();
    	        }
    	    }
    }
	
}