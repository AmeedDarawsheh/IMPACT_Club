package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddNewMemberController {	
	@FXML
	Button cancel,add;
	@FXML
	TextField first,last,ssn,email,ph,address;
	
	
	String firstname,lastname,emaill,addresss;
	int ssnn,phh;
	public void addNewMember(ActionEvent e ) {
		
		firstname = first.getText();
		lastname = last.getText();
		ssnn = Integer.parseInt(ssn.getText());	
		phh = Integer.parseInt(ph.getText());
		addresss = address.getText();
		emaill = email.getText();
		
		
		// Enter SQL Here !!!
		
		
		
	}
		
	public void cancel(ActionEvent e ) {
		
		
		
		
		
		
		
		
	}
		
}