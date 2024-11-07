package application;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class SessionController implements Initializable{
	
	@FXML private Button searchBtn,saveBtn,editBtn,deleteBtn,addBtn;
	@FXML private TableView sessionTable , sessionLeader;
	@FXML private ToggleButton switchBtn;
	//  get column and fill it  
	@FXML private TextField inputIDF,timeF,topicF;
	@FXML private DatePicker date;
	@FXML private Label nameTable; 
	String ID;
	@FXML
	void searchbtn(ActionEvent event) {		
		inputIDF.getText();
		if(true 
				// search if this id is true
				// == Integer.parseInt(inputIDF.getText());
				) {			
			ID =inputIDF.getText();
			editBtn.setDisable(false);
			deleteBtn.setDisable(false);
		}
	}
	@FXML
	void deletebtn(ActionEvent event) {
		
		// delete by "ID"
	}
	
	
	@FXML
	public void editbtn(ActionEvent event) {
		saveBtn.setDisable(false);
		timeF.setDisable(false);
		topicF.setDisable(false);
		date.setDisable(false);
		
		// timeF.setText();
		// topicF.setText();
		// date.setValue();
		
		// update values with same "ID"
		
	}
	@FXML
	public void addbtn(ActionEvent event) {
		saveBtn.setDisable(false);
		timeF.setDisable(false);
		topicF.setDisable(false);
		date.setDisable(false);
		
		//timeF.getText();
		//topicF.getText();
		//date.getValue();
		
		// add values to database by "ID"
		
	}
	
	@FXML
	public void savebtn(ActionEvent event){
	
		//String timeFF=timeF.getText();
	//String topicFF=topicF.getText();
	//	LocalDate datee =date.getValue();	
		
		//SQL save to database	by "ID"
		
		editBtn.setDisable(true);
		deleteBtn.setDisable(true);
		timeF.setDisable(true);
		topicF.setDisable(true);
		date.setDisable(true);	
	}
	
	@FXML
	public void switchbtn(ActionEvent event){
		if(!(switchBtn.isSelected())) {
			sessionTable.setVisible(true);
			sessionLeader.setVisible(false);
			nameTable.setText("Session");
		}
		else {
			sessionTable.setVisible(false);
			sessionLeader.setVisible(true);
			nameTable.setText("Leader");
		}
	}	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		sessionTable.setVisible(true);
		sessionLeader.setVisible(false);
		saveBtn.setDisable(true);
		editBtn.setDisable(true);
		deleteBtn.setDisable(true);
		searchBtn.setDisable(false);
		timeF.setDisable(true);
		topicF.setDisable(true);
		date.setDisable(true);
		nameTable.setText("Session");	
	}	
}