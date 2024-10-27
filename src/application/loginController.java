package application;
import java.io.IOException;

import javax.swing.JTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class loginController {
	private Stage stage ;
	private Scene scene;
	private Parent root;
	@FXML
	TextField emailTextField;
	@FXML
	PasswordField passwordField;
	public void signIn(ActionEvent e) throws IOException {
	//	System.out.println("sign in");
		String username = emailTextField.getText();
		String password = passwordField.getText();	
		
		if(true
				/*username.equals("Admin")&&password.equals("Admin")*/
			) {
			root = FXMLLoader.load(getClass().getResource("MainMenuAdmin.fxml"));
			stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			stage.setTitle("Admin Setup");
			stage.centerOnScreen();
			stage.setResizable(false);
		}	
	}
	public void signUp(ActionEvent e) {
		System.out.println("sign up");		
	
	}
}