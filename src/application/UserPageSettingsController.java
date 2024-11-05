package application;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
public class UserPageSettingsController implements Initializable{	
	@FXML
	Button  savePass , saveAcc , saveApp;
	@FXML
	TextField fName
	,mName
	,lName
	,eField
	,uField
	,SAField
	,CAField
	,Currentpass,newPass,cNewPass;
	
	@FXML
	RadioButton dark,light,fullscreen,miniscreen;
	
	ToggleGroup toggleGroup1 = new ToggleGroup();
	ToggleGroup toggleGroup2 = new ToggleGroup();
	
	@FXML
	public void savePassword(ActionEvent e) {
		String Currentpasss = Currentpass.getText();
		String newPasss = newPass.getText();
		String cNewPasss = cNewPass.getText();
		Boolean b1=false,b2=false;	
		if(true
				//test if current pass is correct
				//Currentpasss = SQL 
				) {
			b1=true;
		}
		if(newPasss.equals(cNewPasss)&&
				newPasss!=null&&
				cNewPasss!=null&&
				newPasss.length()>=8) {
			
			System.out.println("Same");
			
			b2=true;
		}
		
		if(b1&&b2) {
			
			System.out.println("Changed");
			//sql change pass
		}
	}
	@FXML
	public void saveAccount(ActionEvent e) {
		String fNameS = fName.getText();
		String mNameS = mName.getText();
		String lNameS = lName.getText();
		String eFieldS = eField.getText();
		String uFieldS = uField.getText();
		String SAFieldS = SAField.getText();
		String CAFieldS = CAField.getText();
		if(fNameS!=null&&
				mNameS!=null&&
				lNameS!=null&&
				eFieldS!=null&&
				uFieldS!=null&&
				SAFieldS!=null&&
				CAFieldS!=null) {
			
		//Update data By SQl Commands	
			
		System.out.println("saveAcc");			
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
			//scene.setMaximized(true);
			stage.setFullScreen(true);
			//stage.setMaximized(true);
		}
		else {
			System.out.println("mini");
			//stage.setMaximized(false);
			stage.setFullScreen(false);
		}	
		// not ready !!
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		dark.setToggleGroup(toggleGroup1);
		light.setToggleGroup(toggleGroup1);
		fullscreen.setToggleGroup(toggleGroup2);
		miniscreen.setToggleGroup(toggleGroup2);
		
		fName.setText("//get Values By SQl Commands");
		mName.setText("//get Values By SQl Commands");
		lName.setText("//get Values By SQl Commands");
		eField.setText("//get Values By SQl Commands");
		uField.setText("//get Values By SQl Commands");
		SAField.setText("//get Values By SQl Commands");
		CAField.setText("//get Values By SQl Commands");
	}	
}