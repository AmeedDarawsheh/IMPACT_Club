package application;
import java.io.IOException;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
public class UserPageConreoller implements Initializable{
	@FXML
	StackPane stackPane;
	@FXML 
	Stage stage ;
	@FXML
	AnchorPane scenePane;
	 private boolean isDarkTheme = false; 
	 static int points;
	 @FXML
	 Pane pane1,pane2; 
	 @FXML
	 Label name,dateJoin,email,address;
	 @FXML
	 private ToggleButton  themeToggleButton;	
	 @FXML
	    private void toggleTheme() {	 		 
	        Scene scene = themeToggleButton.getScene(); // Get the current scene

	        if (isDarkTheme) {
	            // Switch to light theme
	            scene.getStylesheets().remove("/style.css");
	        //	pane2.setStyle("-fx-background-color: #ffffff;");
	            isDarkTheme = false;
	            themeToggleButton.setText("🌙");
	        } else {
	            // Switch to dark theme
	            scene.getStylesheets().add("/style.css");
	      //  	pane2.setStyle("-fx-background-color: #2E3436;");
	       
	            isDarkTheme = true;
	            themeToggleButton.setText("🌞");
	        }
	    }
	@Override
	public void initialize(URL locatian,ResourceBundle resources) {	
		try {
			Parent fxml = FXMLLoader.load(getClass().getResource("UserPageHome.fxml"));
			stackPane.getChildren().removeAll();
			stackPane.getChildren().setAll(fxml);
			
			loadMemberDetails();
		}catch(IOException ex ) {
		//	Logger.getLogger(SageController.class.getName()).log(Level.SEVERE,null, ex);
		}	
	}
	private void loadMemberDetails() {
    DatabaseConnection database = new DatabaseConnection();
    try (Connection conn = database.getConnection()) {
        String query = "SELECT p.user_name AS email, p.first_name || ' ' || p.middle_name || ' ' || p.last_name AS name, " +
                       "p.city, p.street, m.points, p.start_date " +
                       "FROM \"IMPACT Club\".person p " +
                       "JOIN \"IMPACT Club\".member m ON p.ssn = m.ssn " +
                       "WHERE m.memberid = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, loginController.getLoggedInMemberId());
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            email.setText(rs.getString("email"));
            name.setText(rs.getString("name"));
            String street = rs.getString("street");
            String city = rs.getString("city");
            address.setText( street +  "street,"  + city);
            points=rs.getInt("points");
            System.out.print(points);
            
          dateJoin.setText("Member since: "+rs.getString("start_date"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
	static int getpoints() {
		return points;
	}
	 public void Home(ActionEvent e) throws IOException {	 
		 	Parent fxml = FXMLLoader.load(getClass().getResource("UserPageHome.fxml"));
			stackPane.getChildren().removeAll();
			stackPane.getChildren().setAll(fxml);		
	 	}
	 public void myProject(ActionEvent e) throws IOException {	 
		 	Parent fxml = FXMLLoader.load(getClass().getResource("UserPageProject.fxml"));
			stackPane.getChildren().removeAll();
			stackPane.getChildren().setAll(fxml);	
		}
	 public void plan(ActionEvent e) throws IOException {	
		 	Parent fxml = FXMLLoader.load(getClass().getResource("UserPagePlan.fxml"));
			stackPane.getChildren().removeAll();
			stackPane.getChildren().setAll(fxml);	
	 	}
	 public void settings(ActionEvent e) throws IOException {	
		 	Parent fxml = FXMLLoader.load(getClass().getResource("UserPageSettings.fxml"));
			stackPane.getChildren().removeAll();
			stackPane.getChildren().setAll(fxml);		
	 	} 
	 public void Logout(ActionEvent e) throws IOException {			
		 	Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Logout");
			alert.setHeaderText("You're about to logout!");
			alert.setContentText("Do you want to save before exiting?: ");		
			if(alert.showAndWait().get() == ButtonType.OK){
				stage = (Stage) scenePane.getScene().getWindow();
				System.out.println("You successfully logged out!");
				stage.close();				
				Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();			
			}		
	 	}
}