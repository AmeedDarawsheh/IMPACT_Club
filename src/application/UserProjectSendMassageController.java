package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class UserProjectSendMassageController {

	
	@FXML
	TextArea text;
	
	@FXML
	public void Send(ActionEvent e) {
	
		String feed = text.getText();
		
		// sent text by user Email to all members in same project
		
	}
	@FXML
	public void cancel(ActionEvent e) {
		Scene scene = text.getScene();
		Stage stage = (Stage) scene.getWindow();
		stage.close();
	}
	
	
	
}
