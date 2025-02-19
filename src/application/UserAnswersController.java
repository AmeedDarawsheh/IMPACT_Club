package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class UserAnswersController {
	@FXML
    private TextArea answersTextArea;
	
	String answers;
	int memberId;
	int sessionId;
	boolean project =false;
	
	
	@FXML
	public void cancel(ActionEvent e) {
		Stage stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
	    stage.close();
	}
	

	
	public void setAnswers(int memberId, int sId) {
		 this.memberId = memberId;
		
		
		sessionId=sId;
    }

    @FXML
    private void submitAnswers() {
    	
        String answers= answersTextArea.getText();

   
    	 String query = """
    	            UPDATE "IMPACT Club".sessionmember
    	            SET answers = ?
    	            WHERE memberid = ? AND sessionid = ?;
    	            """;
    	 
    	        try (Connection conn = DatabaseConnection.getConnection();
    	             PreparedStatement stmt = conn.prepareStatement(query)) {

    	            stmt.setString(1, answers);
    	            stmt.setInt(2, memberId);
    	            stmt.setInt(3, sessionId);

    	            int rowsAffected = stmt.executeUpdate();
    	            if (rowsAffected > 0) {
    	                // Show success alert
    	                Alert alert = new Alert(Alert.AlertType.INFORMATION);
    	                alert.setTitle("Answers Submitted");
    	                alert.setHeaderText(null);
    	                alert.setContentText("Your Answers has been saved successfully.");
    	                alert.showAndWait();
    	               
    	            }
    	        } catch (SQLException e) {
    	            e.printStackTrace();
    	          
    	            Alert alert = new Alert(Alert.AlertType.ERROR);
    	            alert.setTitle("Database Error");
    	            alert.setHeaderText(null);
    	            alert.setContentText("Failed to save Answers. Please try again.");
    	            alert.showAndWait();
    	        }
    	    
    }
	
}
