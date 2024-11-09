package application;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
public class UserPageSettingsController implements Initializable{	
	@FXML
	Button  savePass , saveAcc , saveApp;
	@FXML
	TextField fName
	,mName
	,lName
	,pField
	,uField
	,SAField
	,CAField
	,Currentpass,newPass,cNewPass;
	
	FileChooser fileChooser ;
	@FXML 
	ImageView image1;
	DatabaseConnection data=new DatabaseConnection();
	Connection conn = data.getConnection();
	@FXML
	RadioButton dark,light,fullscreen,miniscreen;
	
	ToggleGroup toggleGroup1 = new ToggleGroup();
	ToggleGroup toggleGroup2 = new ToggleGroup();

@FXML
public void savePassword(ActionEvent e) {
    String currentPass = Currentpass.getText();
    String newPasss = newPass.getText();
    String confirmNewPass = cNewPass.getText();
    boolean isCurrentPassCorrect = false;
    boolean isNewPassValid = false;

    
    String checkPassSql = "SELECT password FROM \"IMPACT Club\".person p JOIN \"IMPACT Club\".member m ON p.ssn = m.ssn WHERE m.memberid = ?";
    
    try (
         PreparedStatement checkStmt = conn.prepareStatement(checkPassSql)) {

        checkStmt.setInt(1, loginController.getLoggedInMemberId()); 
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next() && rs.getString("password").equals(currentPass)) {
            isCurrentPassCorrect = true;
        } else {
            showAlert(AlertType.WARNING, "Incorrect Password", "The current password is incorrect.");
            return;
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        showAlert(AlertType.ERROR, "Error", "An error occurred while verifying the current password.");
        return;
    }

    // Validate new password
    if (newPasss.equals(confirmNewPass) && newPasss != null ) {
        isNewPassValid = true;
    } else {
        showAlert(AlertType.WARNING, "Invalid Password", "New passwords do not match  please confirm it.");
        
        return;
    }

    // If both conditions are met, update the password
    if (isCurrentPassCorrect && isNewPassValid) {
        String updatePassSql = "UPDATE \"IMPACT Club\".person SET password = ? WHERE ssn = (SELECT ssn FROM \"IMPACT Club\".member WHERE memberid = ?)";

        try (PreparedStatement updateStmt = conn.prepareStatement(updatePassSql)) {
            updateStmt.setString(1, newPasss);
            updateStmt.setInt(2, loginController.getLoggedInMemberId());

            int rowsAffected = updateStmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Password changed successfully.");
            } else {
                showAlert(AlertType.WARNING, "Update Failed", "Password could not be updated.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "An error occurred while updating the password.");
        }
    }
}
	@FXML
	public void saveAccount(ActionEvent e) {
	    String fNameS = fName.getText();
	    String mNameS = mName.getText();
	    String lNameS = lName.getText();
	    String pFieldS = pField.getText(); 
	    String uFieldS = uField.getText();
	    String SAFieldS = SAField.getText();
	    String CAFieldS = CAField.getText();

	    if (fNameS != null && mNameS != null && lNameS != null && pFieldS != null && uFieldS != null && SAFieldS != null && CAFieldS != null) {
	       
	        String sql = "UPDATE \"IMPACT Club\".person SET first_name = ?, middle_name = ?, last_name = ?, phone_number = ?, user_name = ?, street = ?, city = ? WHERE ssn = (SELECT ssn FROM \"IMPACT Club\".member WHERE memberid = ?)";

	        try (Connection conn = data.getConnection(); 
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {

	            // Set parameters for the update
	            pstmt.setString(1, fNameS);
	            pstmt.setString(2, mNameS);
	            pstmt.setString(3, lNameS);
	            pstmt.setString(4, pFieldS);
	            pstmt.setString(5, uFieldS);
	            pstmt.setString(6, SAFieldS);
	            pstmt.setString(7, CAFieldS);
	            pstmt.setInt(8, loginController.getLoggedInMemberId()); 
	            int rowsAffected = pstmt.executeUpdate();

	            if (rowsAffected > 0) {
	                showAlert(AlertType.INFORMATION, "Success", "Account information updated successfully.");
	            } else {
	                showAlert(AlertType.WARNING, "Update Failed", "No record found for the given member ID.");
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	            showAlert(AlertType.ERROR, "Error", "An error occurred while updating the account information.");
	        }
	    } else {
	        showAlert(AlertType.WARNING, "Validation Error", "All fields must be filled in to save account settings.");
	    }
	}

	// Method to show alerts
	private void showAlert(AlertType alertType, String title, String message) {
	    Alert alert = new Alert(alertType);
	    alert.setTitle(title);
	    alert.setHeaderText(null); // No header
	    alert.setContentText(message);
	    alert.showAndWait();
	}
	@FXML
	public void saveApp(ActionEvent e) {
		System.out.println("saveApp");
		Scene scene = miniscreen.getScene();
		Stage stage = (Stage) scene.getWindow();
		
		if(dark.isSelected()) {
			scene.getStylesheets().add("/style.css");
		}
		else {
			scene.getStylesheets().remove("/style.css");
		}		
		if(fullscreen.isSelected()) {
			System.out.println("full");
			//scene.setMaximized(true);
			stage.setFullScreen(true);
			//stage.setMaximized(true);
		}
		else {
			System.out.println("mini");
			//stage.setMaximized(false);
			stage.setFullScreen(false);
		}	
		// not ready !!
	}
    @FXML
    void selectPhoto(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) image1.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            Image img = new Image(selectedFile.toURI().toString());
            // store img to database to add it in another place 
            image1.setImage(img);
            Circle clip = new Circle(image1.getFitWidth() / 2, image1.getFitHeight() / 2,
                    Math.min(image1.getFitWidth(), image1.getFitHeight()) / 2);
            image1.setClip(clip);
        }
    }	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		dark.setToggleGroup(toggleGroup1);
		light.setToggleGroup(toggleGroup1);
		fullscreen.setToggleGroup(toggleGroup2);
		miniscreen.setToggleGroup(toggleGroup2);
		
		// image1.setImage();  member image 
		
		loadMemberDetails();
	}	
	private void loadMemberDetails() {
		String sql = "SELECT first_name, middle_name, last_name, user_name,phone_number,  street, city FROM \"IMPACT Club\".person p "
	               + "JOIN \"IMPACT Club\".member m ON p.ssn = m.ssn WHERE m.memberid = ?";
	    try (Connection conn = data.getConnection(); 
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setInt(1, loginController.getLoggedInMemberId());

	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            // Set text fields with retrieved values
	            fName.setText(rs.getString("first_name"));
	            mName.setText(rs.getString("middle_name"));
	            lName.setText(rs.getString("last_name"));
	            pField.setText(rs.getString("phone_number"));
	            
	            uField.setText(rs.getString("user_name"));
	            SAField.setText(rs.getString("street"));
	            CAField.setText(rs.getString("city"));
	        } else {
	            System.out.println("No member found with ID: " + loginController.getLoggedInMemberId());
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
}}