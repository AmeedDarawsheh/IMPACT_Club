package application;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
public class ProjectDetailController implements Initializable {
	@FXML
	private Button goals,financialplan,leaderofproject,memberinproject,backmenu;
	@FXML
	private AnchorPane scenePane;
	Stage stage;		
	@FXML
	private StackPane stackPane;
	@FXML
	Label title;
	@FXML
	Button addBtn;
	public int projectId;
	String currentPage;
	public void setProjectId(int projectId) {
        this.projectId = projectId;
        System.out.print(projectId);
        
    }

	public void initialize(URL locatian,ResourceBundle resources) {	
		//try {
			 
			//FXMLLoader loader = new FXMLLoader(getClass().getResource("Goals.fxml"));
		    //Parent fxml = loader.load();
		    //AboutController aboutController = loader.getController();
		   // aboutController.setProjectId(projectId);
		stackPane.getChildren().removeAll();
		//stackPane.getChildren().setAll(fxml);
		title.setText("About");
		addBtn.setVisible(false);
		addBtn.setText("Add Plan");
		//}//catch(IOException ex ) {
		//	Logger.getLogger(SageController.class.getName()).log(Level.SEVERE,null, ex);
		//}	
	}
	public void about(ActionEvent e ) throws IOException {	
		 FXMLLoader loader = new FXMLLoader(getClass().getResource("Goals.fxml"));
		    Parent fxml = loader.load();
		 AboutController aboutController = loader.getController();
		    aboutController.setProjectId(projectId);

		stackPane.getChildren().removeAll();
		stackPane.getChildren().setAll(fxml);
		title.setText("About");
		addBtn.setVisible(false);
		addBtn.setText("Add Plan");
	}
	public void financialplan(ActionEvent e ) throws IOException {		
		
		 FXMLLoader loader = new FXMLLoader(getClass().getResource("financialPlan.fxml"));
		    Parent fxml = loader.load();
		    FinancialPlan financialController = loader.getController();
		    financialController.setProjectId(projectId);

		stackPane.getChildren().removeAll();
		stackPane.getChildren().setAll(fxml);
		title.setText("Financial Plan");
		addBtn.setVisible(true);
		addBtn.setText("Add Member");
		
	}
	public void leaderofproject(ActionEvent e ) throws IOException {		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LeaderofProject.fxml"));
	    Parent fxml = loader.load();
	    leaderofprojectController leaderofprojectController = loader.getController();
	  
	    leaderofprojectController.setProjectId(projectId);

		stackPane.getChildren().removeAll();
		stackPane.getChildren().setAll(fxml);
		title.setText("Leader of project ");
		addBtn.setVisible(true);
		addBtn.setText("Add Plan");
	}
	public void memberinproject(ActionEvent e ) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MemberinProject.fxml"));
	    Parent fxml = loader.load();
	    MemberinprojectController memberinprojectController = loader.getController();
	    memberinprojectController.setProjectId(projectId);
         currentPage="memberinproject";
		
		stackPane.getChildren().removeAll();
		stackPane.getChildren().setAll(fxml);
		title.setText("Member in Project");
		addBtn.setVisible(true);
		addBtn.setText("Add Member to  Project");
	}	
	public void logout(ActionEvent e) {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("You're about to logout!");
		alert.setContentText("Do you want to save before exiting?: ");
		
		if(alert.showAndWait().get() == ButtonType.OK){
			stage = (Stage) scenePane.getScene().getWindow();
			System.out.println("You successfully logged out!");
			stage.close();
		}				
	}
	  @FXML
	    public void handleAddButton(ActionEvent e) throws IOException {
	        switch (currentPage) {
	            case "memberinproject":
	                //openAddMemberPage();
	                break;
	            case "Projects":
	              //  openAddProjectPage();
	                break;
	            
	            
	            default:
	                break;
	        }
	    }
	  
}