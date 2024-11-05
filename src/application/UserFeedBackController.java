package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class UserFeedBackController implements Initializable{

	@FXML
	ListView <String>listOfProjects;
	@FXML
	TextArea text;
	
	@FXML
	public void Send(ActionEvent e) {
	
		String feed = text.getText();
		
		// get user email  and send email to club  have a subject "feedback from user " and text 
		
		
	}
	@FXML
	public void cancel(ActionEvent e) {
		Scene scene = listOfProjects.getScene();
		Stage stage = (Stage) scene.getWindow();
		stage.close();
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		// fill User Projects Done Only
		
	}	
}