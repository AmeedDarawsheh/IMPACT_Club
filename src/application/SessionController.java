package application;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class SessionController implements Initializable{
	
	@FXML private Button searchBtn,saveBtn,editBtn,deleteBtn,addBtn,leaderSave;
	@FXML private TableView sessionTable , sessionLeader;
	@FXML private ToggleButton switchBtn;
	//  get column and fill it  
	@FXML private TextField IN,F1,F2; //IN INPUT ID Leader / session //F1-> Topic / Role  #  F2-> Time /Attunes 
	@FXML private DatePicker date;
	@FXML private Label nameTable;
    Boolean isleader = false ;
	String ID;
	@FXML
	void searchbtn(ActionEvent event) {		
		IN.getText();	
		if(istrue(IN.getText())){			
			if(isleader) {
				ID =IN.getText();
								
				// SQL 	add values to fields					
			}
			else {			
				
				ID =IN.getText();
			
				
				// SQL 	add values to fields
				
			}
		}
		
		else {
			
		}
	}
	@FXML
	void deletebtn(ActionEvent event) {	
		saveBtn.setDisable(true);
		editBtn.setDisable(true);
		F2.setDisable(true);
		F1.setDisable(true);
		date.setDisable(true);
		if(istrue(IN.getText())) {
			if(isleader) {				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Delete");
				alert.setHeaderText("You're about to Delete  Leader Session ");
				alert.setContentText("Do you sure about that ? :");		
				if(alert.showAndWait().get() == ButtonType.OK){

					 // SQL Leader Page
				
				}
			}
			else{			
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Delete");
				alert.setHeaderText("You're about to Delete Session ");
				alert.setContentText("Do you sure about that ? :");		
				if(alert.showAndWait().get() == ButtonType.OK){

					//SQL session page
				
				}			
			}			
		}		
	}	
	@FXML
	public void editbtn(ActionEvent event) {
		saveBtn.setDisable(false);
		F2.setDisable(false);
		F1.setDisable(false);	
		if(isleader) {
			date.setVisible(false);
			
			
			
		}else {
			date.setVisible(true);
			date.setDisable(false);
		}		
	}
	@FXML
	public void addbtn(ActionEvent event) {
		saveBtn.setDisable(false);
		F2.setDisable(false);
		F1.setDisable(false);
		date.setDisable(false);		
		if(isleader) {
			date.setVisible(false);
			//F2.getText();
			//F1.getText();
			//date.getValue();		
			// add values to database by "ID"
		}
		else{
			date.setVisible(true);
			date.setDisable(false);
			//F2.getText();
			//F1.getText();
			//date.getValue();		
			//add values to database by "ID"			
		}	
	}	
	@FXML
	public void savebtn(ActionEvent event){		
		editBtn.setDisable(true);
		deleteBtn.setDisable(true);
		F2.setDisable(true);
		F1.setDisable(true);
		date.setDisable(true);
		
		if(isleader) {
			date.setVisible(false);
			//F2.getText();
			//F1.getText();
			//date.getValue();		
			// add values to database by new "ID"
		}
		else{
			date.setVisible(true);
			date.setDisable(true);
			//F2.getText();
			//F1.getText();
			//date.getValue();		
			//add values to database by "ID"			
		}
	}	
	@FXML
	public void switchbtn(ActionEvent event){
		if(!(switchBtn.isSelected())) {
			sessionTable.setVisible(true);
			sessionLeader.setVisible(false);
			nameTable.setText("Session");
			isleader = false ;
			date.setVisible(true);
			F1.setPromptText("Topic");
			F2.setPromptText("Time");
			IN.setText(null);
			F1.setText(null);
			F2.setText(null);
			date.setValue(null);
		}
		else {
			sessionTable.setVisible(false);
			sessionLeader.setVisible(true);
			nameTable.setText("Leader");
			isleader = true;
			date.setVisible(false);
			F1.setPromptText("Role");
			F2.setPromptText("Attunes");
			IN.setText(null);
			F1.setText(null);
			F2.setText(null);
		}
	}	
	private boolean istrue( String ID ) {
		boolean res = false;
		
		if(true
				
				//SQL ID is true or not 
				
				) {
			editBtn.setDisable(false);
			deleteBtn.setDisable(false);
			res=true;
		}
		else{
		res=false;
		editBtn.setDisable(true);
		deleteBtn.setDisable(true);
				}	
		return res;
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		sessionTable.setVisible(true);
		sessionLeader.setVisible(false);
		saveBtn.setDisable(true);
		editBtn.setDisable(true);
		deleteBtn.setDisable(true);
		searchBtn.setDisable(false);
		F2.setDisable(true);
		F1.setDisable(true);
		date.setDisable(true);
		nameTable.setText("Session");	
	}	
}