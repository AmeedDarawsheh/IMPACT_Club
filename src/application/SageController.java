package application;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
public class SageController implements Initializable {
	@FXML
	private Button overviewB,logoutB,membersB,projectsB,plansB;
	@FXML
	private AnchorPane scenePane;
	Stage stage;		
	@FXML
	private StackPane stackPane;
	@FXML
	Label title;
	@FXML
	Button addBtn;
	private String currentPage;
	@Override
	public void initialize(URL locatian,ResourceBundle resources) {	
		try {
			Parent fxml = FXMLLoader.load(getClass().getResource("Overview.fxml"));
			stackPane.getChildren().removeAll();
			stackPane.getChildren().setAll(fxml);
			title.setText("OverView");
		}catch(IOException ex ) {
		//	Logger.getLogger(SageController.class.getName()).log(Level.SEVERE,null, ex);
		}	
	}
	public void overviewB(ActionEvent e ) throws IOException {		
		Parent fxml = FXMLLoader.load(getClass().getResource("Overview.fxml"));
		stackPane.getChildren().removeAll();
		stackPane.getChildren().setAll(fxml);
		title.setText("OverView");
		addBtn.setVisible(false);
		addBtn.setText("Add Plan");
	}
	public void membersB(ActionEvent e ) throws IOException {		
		Parent fxml = FXMLLoader.load(getClass().getResource("Members.fxml"));
		stackPane.getChildren().removeAll();
		stackPane.getChildren().setAll(fxml);
		title.setText("Members");
		addBtn.setVisible(true);
		addBtn.setText("Add Member");
		currentPage = "Members";
		
	}
	public void plansB(ActionEvent e ) throws IOException {		
		Parent fxml = FXMLLoader.load(getClass().getResource("Plans.fxml"));
		stackPane.getChildren().removeAll();
		stackPane.getChildren().setAll(fxml);
		title.setText("Plans");
		addBtn.setVisible(true);
		addBtn.setText("Add Plan");
	}
	public void projectsB(ActionEvent e ) throws IOException {	
		Parent fxml = FXMLLoader.load(getClass().getResource("Projects.fxml"));
		stackPane.getChildren().removeAll();
		stackPane.getChildren().setAll(fxml);
		title.setText("Projects");
		addBtn.setVisible(true);
		addBtn.setText("Add Project");
		 currentPage = "Projects"; 
	}	
	public void logout(ActionEvent e) throws IOException {	
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
	  @FXML
	    public void handleAddButton(ActionEvent e) throws IOException {
	        switch (currentPage) {
	            case "Members":
	                openAddMemberPage();
	                break;
	            case "Projects":
	              //  openAddProjectPage();
	                break;
	            
	            
	            default:
	                break;
	        }
	    }

	    private void openAddMemberPage() throws IOException {
	        Parent fxml = FXMLLoader.load(getClass().getResource("AddNewMember.fxml"));
	        Stage addMemberStage = new Stage();
	        addMemberStage.setTitle("Add Member");
	        addMemberStage.setScene(new Scene(fxml));
	        addMemberStage.show();
	    }

	   /* private void openAddProjectPage() throws IOException {
	        Parent fxml = FXMLLoader.load(getClass().getResource("AddProject.fxml"));
	        Stage addProjectStage = new Stage();
	        addProjectStage.setTitle("Add Project");
	        addProjectStage.setScene(new Scene(fxml));
	        addProjectStage.show();
	    }*/

}