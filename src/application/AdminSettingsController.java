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
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AdminSettingsController implements Initializable {
	DatabaseConnection data =new DatabaseConnection();
	Connection conn=data.getConnection();
	@FXML
	Button  savePass , saveAcc , saveApp;
	@FXML
	TextField fName
	,mName
	,lName
	,eField
	,mField
	,SAField
	,CAField
	,Currentpass,newPass,cNewPass;
	
	FileChooser fileChooser ;
	@FXML 
	ImageView image1;
	
	@FXML
	RadioButton dark,light,fullscreen,miniscreen;
	
	ToggleGroup toggleGroup1 = new ToggleGroup();
	ToggleGroup toggleGroup2 = new ToggleGroup();
	
	@FXML
	public void saveAccount(ActionEvent e) {
	    String fNameS = fName.getText();
	    String mNameS = mName.getText();
	    String lNameS = lName.getText();
	    String eFieldS = eField.getText();
	    String uFieldS = mField.getText();
	    String SAFieldS = SAField.getText();
	    String CAFieldS = CAField.getText();

	    if (fNameS == null || mNameS == null || lNameS == null || eFieldS == null || 
	        uFieldS == null || SAFieldS == null || CAFieldS == null) {
	        showAlert("Please fill in all fields before saving.");
	        return;
	    }

	   
	    String updatePersonQuery = "UPDATE \"IMPACT Club\".person SET first_name = ?, middle_name = ?, last_name = ?, " +
	                               "user_name = ?, street = ?, city = ? " +
	                               "WHERE ssn = (SELECT ssn FROM \"IMPACT Club\".leader WHERE leaderid = ?)";

	    
	    String updateLeaderQuery = "UPDATE \"IMPACT Club\".leader SET major = ? WHERE leaderid = ?";

	    try {
	        conn.setAutoCommit(false); 

	       
	        try (PreparedStatement psPerson = conn.prepareStatement(updatePersonQuery)) {
	            psPerson.setString(1, fNameS);
	            psPerson.setString(2, mNameS);
	            psPerson.setString(3, lNameS);
	            psPerson.setString(4, eFieldS);
	            psPerson.setString(5, SAFieldS);
	            psPerson.setString(6, CAFieldS);
	            psPerson.setInt(7, loginController.getLoggedInLeaderId());  
	            psPerson.executeUpdate();
	        }

	   
	        try (PreparedStatement psLeader = conn.prepareStatement(updateLeaderQuery)) {
	            psLeader.setString(1, uFieldS);  
	            psLeader.setInt(2, loginController.getLoggedInLeaderId());
	            psLeader.executeUpdate();
	        }

	        conn.commit(); 
	        showAlert("Account settings saved successfully.");
	    } catch (SQLException ex) {
	        try {
	            conn.rollback();  
	        } catch (SQLException rollbackEx) {
	            rollbackEx.printStackTrace();
	        }
	        ex.printStackTrace();
	        showAlert("An error occurred while updating account settings.");
	    } finally {
	        try {
	            conn.setAutoCommit(true);  
	        } catch (SQLException autoCommitEx) {
	            autoCommitEx.printStackTrace();
	        }
	    }
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
			
			stage.setFullScreen(true);
		}
		else {
			System.out.println("mini");
	
			stage.setFullScreen(false);
		}	
	
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
		dark.setDisable(true);
		light.setDisable(true);
		fullscreen.setDisable(true);
		miniscreen.setDisable(true);
		// image1.setImage();  member image 
		
		String query = "SELECT first_name, middle_name, last_name,  user_name, street, city, major " +
                "FROM \"IMPACT Club\".person p " +
                "JOIN \"IMPACT Club\".leader l ON p.ssn = l.ssn " +
                "WHERE l.leaderid = ?";
 
 try (PreparedStatement ps = conn.prepareStatement(query)) {
     ps.setInt(1, loginController.getLoggedInLeaderId());
     ResultSet rs = ps.executeQuery();
     
     if (rs.next()) {
         // Set values in the fields
         fName.setText(rs.getString("first_name"));
         mName.setText(rs.getString("middle_name"));
         lName.setText(rs.getString("last_name"));
         eField.setText(rs.getString("user_name"));
         mField.setText(rs.getString("major"));
         SAField.setText(rs.getString("street"));
         CAField.setText(rs.getString("city"));
        
         
         // Load and set the image (assuming image loading logic based on user data)
         // Image image = new Image("path/to/image"); // Set appropriate path
         // image1.setImage(image); 
     }
 } catch (SQLException e) {
     e.printStackTrace();
     showAlert("An error occurred while loading the account information.");
 }
		/*fName.setText("//get Values By SQl Commands");
		mName.setText("//get Values By SQl Commands");
		lName.setText("//get Values By SQl Commands");
		eField.setText("//get Values By SQl Commands");
		uField.setText("//get Values By SQl Commands");
		SAField.setText("//get Values By SQl Commands");
		CAField.setText("//get Values By SQl Commands");*/
	}
	public void showAlert(String message) {
	    Alert alert = new Alert(AlertType.INFORMATION);
	    alert.setTitle("Information");
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}	
	@FXML
	public void onEditPasswordButtonClick(ActionEvent event) {
	    String currentPassword = Currentpass.getText().trim();
	    String newPassword = newPass.getText().trim();
	    String confirmNewPassword = cNewPass.getText().trim();
	    
	    if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
	        showAlert("Please fill all password fields.");
	        return;
	    }

	    if (!newPassword.equals(confirmNewPassword)) {
	        showAlert("New Password and Confirm Password do not match.");
	        return;
	    }

	    int leaderId = loginController.getLoggedInLeaderId(); // Assuming you have a method to get the logged-in leader's ID

	    String query = "SELECT password FROM \"IMPACT Club\".person WHERE ssn = (SELECT ssn FROM \"IMPACT Club\".leader WHERE leaderid = ?)";
	    String updateQuery = "UPDATE \"IMPACT Club\".person SET password = ? WHERE ssn = (SELECT ssn FROM \"IMPACT Club\".leader WHERE leaderid = ?)";

	    try (PreparedStatement ps = conn.prepareStatement(query);
	         PreparedStatement updatePs = conn.prepareStatement(updateQuery)) {
	        
	        // Check current password
	        ps.setInt(1, leaderId);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            String storedPassword = rs.getString("password");
	            if (!storedPassword.equals(currentPassword)) {
	                showAlert("Current Password is incorrect.");
	                return;
	            }

	            // Update to new password
	            updatePs.setString(1, newPassword);
	            updatePs.setInt(2, leaderId);
	            int result = updatePs.executeUpdate();

	            if (result > 0) {
	                showAlert("Password updated successfully.");
	            } else {
	                showAlert("Failed to update password.");
	            }
	        } else {
	            showAlert("User not found.");
	        }
	    } catch (SQLException e) {
	       
	        showAlert("An error occurred while updating the password.");
	    }
	}
}