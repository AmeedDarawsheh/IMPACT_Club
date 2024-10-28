package application;
import java.io.IOException;
import java.lang.System.Logger;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
	
	@Override
	public void initialize(URL locatian,ResourceBundle resources) {	
		try {
			Parent fxml = FXMLLoader.load(getClass().getResource("Overview.fxml"));
			stackPane.getChildren().removeAll();
			stackPane.getChildren().setAll(fxml);
			//title.setText("OverView");

		}catch(IOException ex ) {
		//	Logger.getLogger(SageController.class.getName()).log(Level.SEVERE,null, ex);
		}	
	}
	public void overviewB(ActionEvent e ) throws IOException {		
		Parent fxml = FXMLLoader.load(getClass().getResource("Overview.fxml"));
		stackPane.getChildren().removeAll();
		stackPane.getChildren().setAll(fxml);
		//title.setText("OverView");
	}
	public void membersB(ActionEvent e ) throws IOException {		
		Parent fxml = FXMLLoader.load(getClass().getResource("Members.fxml"));
		stackPane.getChildren().removeAll();
		stackPane.getChildren().setAll(fxml);
		//title.setText("Members");
	}
	public void plansB(ActionEvent e ) throws IOException {		
		Parent fxml = FXMLLoader.load(getClass().getResource("Plans.fxml"));
		stackPane.getChildren().removeAll();
		stackPane.getChildren().setAll(fxml);
		//title.setText("Plans");
	}
	public void projectsB(ActionEvent e ) throws IOException {
		
		Parent fxml = FXMLLoader.load(getClass().getResource("Projects.fxml"));
		stackPane.getChildren().removeAll();
		stackPane.getChildren().setAll(fxml);
		//title.setText("Projects");
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
}