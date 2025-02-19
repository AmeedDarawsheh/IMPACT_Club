package application;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class UserAddProjectController implements Initializable{

	@FXML
	ListView <String>listOfProjects;
	
	@FXML
	public void Add(ActionEvent e) {
		
		
		
		
	}
	@FXML
	public void cancel(ActionEvent e) {
		Scene scene = listOfProjects.getScene();
		Stage stage = (Stage) scene.getWindow();
		stage.close();
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
		
		
	}
}
