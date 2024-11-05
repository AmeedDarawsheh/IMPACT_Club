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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
public class UserPageProjectController implements Initializable {	
	
	
	
	@FXML
    private TableView<Member> tableViewMemberSameProject;
    
    @FXML
    private TableView<Member> tableViewProjectsMemberJoined;
    
    @FXML
    private TableColumn<Member, String> NameColumn;
    
    @FXML
    private TableColumn<Member, String> EmailColumn;
    
    @FXML
    private TableColumn<Member, String> TierColumn;
    
    //  @FXML
    //private TableColumn<Button> ActionColumn;
	
	@FXML
    private TableColumn<Project, String> ProjectNameColumn;
    @FXML
    private TableColumn<Project, String> StateColumn;

    
    
    
    
	@FXML
	public void addProject(ActionEvent e) throws IOException {	
		// dialog have many of projects from plan  and member select one	
		Parent root = FXMLLoader.load(getClass().getResource("UserAddProject.fxml"));
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();	
	}
	@FXML
	public void feedBack(ActionEvent e) throws IOException {
		
		
		Parent root = FXMLLoader.load(getClass().getResource("UserFeedBack.fxml"));
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
		
		
		// dialog select project and send feed about it by email to admins
		
	}
	@FXML
	public void sendMassage(ActionEvent e) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("UserProjectSendMassage.fxml"));
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
		
		// dialog send massage by email to all members in selected project   like : hey friends deadline soon 
		
	}
	
	@FXML
	public void reportE(ActionEvent e) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Report");
		alert.setHeaderText("You're about to Send Report Via Email!");
		alert.setContentText("Are you sure About it ?");	
		if(alert.showAndWait().get() == ButtonType.OK){			
			//jasperreport send via email from club to user about projects 
			
			//SQL	
		}	
	}	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
		
		
		//SQL Tables Fill
		
		
		
		
	}
}