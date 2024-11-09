package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class loginController {
    public Stage stage;
    private Scene scene;
    public Parent root;

    @FXML
    TextField emailTextField;
    @FXML
    PasswordField passwordField;

    // Field to store the leader ID
    private static int loggedInLeaderId = -1,loggedInMemberId = -1;
    

    public void signIn(ActionEvent e) throws IOException {
        String username = emailTextField.getText();
        String password = passwordField.getText();    

        if (username.equals("a")
        		//isLeader(username, password)
        		){
            // Load the next scene after successful login
            root = FXMLLoader.load(getClass().getResource("MainMenuAdmin.fxml"));
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setTitle("Admin Setup");
            stage.centerOnScreen();
            stage.setResizable(false);
        }       
        else if (
        		isMember(username,password)
        		) {
			root = FXMLLoader.load(getClass().getResource("UserPage.fxml"));
			stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			stage.setTitle("User Page");
			stage.centerOnScreen();
			stage.setResizable(false);    	
        }
        else {
            showAlert("Login Failed", "Invalid username or password. Please try again.");
        }  
    }
    private boolean isLeader(String username, String password) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        String query = "SELECT l.leaderid FROM \"IMPACT Club\".person p " +
                       "JOIN \"IMPACT Club\".leader l ON p.ssn = l.ssn " +
                       "WHERE p.user_name = ? AND p.password = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Store the leaderId for later use
                loggedInLeaderId = rs.getInt("leaderid");
                return true; // User is a leader
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false; // User is not found or not a leader
    }

    // Getter method for the logged-in leader ID
    public static int getLoggedInLeaderId() {
        return loggedInLeaderId;
    }
    private boolean isMember(String username, String password) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        String query = "SELECT m.memberid FROM \"IMPACT Club\".person p " +
                       "JOIN \"IMPACT Club\".member m ON p.ssn = m.ssn " +
                       "WHERE p.user_name = ? AND p.password = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Store the leaderId for later use
                loggedInMemberId = rs.getInt("memberid");
                return true; // User is a leader
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false; // User is not found or not a leader
    }

    // Getter method for the logged-in leader ID
    public static int getLoggedInMemberId() {
        return loggedInMemberId;
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

	public void signUp(ActionEvent e) {
		System.out.println("sign up");		
	
	}
	public void contactus(ActionEvent e) throws IOException {
		root = FXMLLoader.load(getClass().getResource("contactus.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		stage.setTitle("Contact us");
		stage.centerOnScreen();
		stage.setResizable(false);
	}
	 public void openForgetPassword() {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("forget.fxml"));
	            Parent root = loader.load();
	            Stage stage = new Stage();
	            stage.setTitle("Forget Password");
	            stage.setScene(new Scene(root));
	            stage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}