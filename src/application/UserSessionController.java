package application;
import javafx.collections.FXCollections;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import java.io.InputStream;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JFrame;
public class UserSessionController implements Initializable{
@FXML
private TableView<SessionUser> sessionTable;
@FXML
private TableColumn<SessionUser, Integer> sessionIdColumn;
@FXML
private TableColumn<SessionUser, String> sessionNameColumn;
@FXML
private TableColumn<SessionUser, String> answersColumn;
@FXML
private TableColumn<SessionUser, String> attendanceColumn;
@FXML
private TableColumn<SessionUser, String> feedbackColumn;
@FXML 
private TextField sessionIdTextField;
ObservableList<SessionUser> sessions;
DatabaseConnection data =new DatabaseConnection();
private SessionUser selectedSession ;
@Override
public void initialize(URL arg0, ResourceBundle arg1) {
	// TODO Auto-generated method stub
	
	sessions = FXCollections.observableArrayList();
    // Set up the columns to match the fields in the SessionData class
    sessionIdColumn.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
    sessionNameColumn.setCellValueFactory(new PropertyValueFactory<>("sessionName"));
    answersColumn.setCellValueFactory(new PropertyValueFactory<>("answers"));
    attendanceColumn.setCellValueFactory(new PropertyValueFactory<>("attendance"));
    feedbackColumn.setCellValueFactory(new PropertyValueFactory<>("feedback"));
    sessions = FXCollections.observableArrayList();
    
    loadSessionData();
    sessionTable.setItems(sessions);
    sessionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        selectedSession = newSelection;});
}
@FXML
private void reload() {
	loadSessionData();
}
private void loadSessionData() {
    
    sessions.clear();
    
    String query = """
        SELECT s.sessionid, s.topic AS sessionname, sm.answers, sm.attendance, sm.feedback
        FROM "IMPACT Club".session s
        JOIN "IMPACT Club".sessionmember sm ON s.sessionid = sm.sessionid
        WHERE sm.memberid = ?
        ORDER BY s.sessionid ASC;
        """;

    try (Connection conn = data.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
    	
        int memberId = loginController.getLoggedInMemberId();
        
        
        stmt.setInt(1, memberId);  
        ResultSet rs = stmt.executeQuery();
       

        
        while (rs.next()) {
            
            int sessionId = rs.getInt("sessionid");
            String sessionName = rs.getString("sessionname");
            String answers = rs.getString("answers");
            String attendance = rs.getString("attendance");
            String feedback = rs.getString("feedback");

            sessions.add(new SessionUser(sessionId, sessionName, answers, attendance, feedback));
          
        }
        
        
    } catch (SQLException e) {
       // e.printStackTrace();
    }
}
@FXML
private void handleReportSessionButtonClick() {
    try {
       DatabaseConnection data =new DatabaseConnection();
       Connection nn=data.getConnection();
       InputStream input =new FileInputStream("C:\\Users\\User\\JaspersoftWorkspace\\Emp\\Leaf_Violet.jrxml");
       JasperDesign jd =JRXmlLoader.load(input);
       JasperReport jr =JasperCompileManager.compileReport(jd);
       JasperPrint jp=JasperFillManager.fillReport(jr, null,nn);
       OutputStream os =new FileOutputStream(new File ("rep.pdf"));
      JasperExportManager.exportReportToPdfStream(jp,os);
       /*JFrame frame =new JFrame ("report");
       JasperViewer viewer = new JasperViewer(jp, false);
      frame.getContentPane().add(viewer);
       frame.pack();
      frame.setVisible(true);*/
       
    } catch (Exception e) {
        //System.err.println("Error displaying report: " + e.getMessage());
    }
}


@FXML
public void feedBack(ActionEvent e) throws IOException {
	
	
	 if (selectedSession != null) {  
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserFeedBack.fxml"));
	        Parent root = loader.load();

	        
	        UserFeedBackController feedbackController = loader.getController();
	        feedbackController.setFeedback(loginController.getLoggedInMemberId(),selectedSession.getSessionId(),false);  
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.show();
	    } else {
	    	 Alert alert = new Alert(Alert.AlertType.WARNING);
	         alert.setTitle("No Session Selected");
	         alert.setHeaderText(null);
	         alert.setContentText("Please select a session to view feedback.");
	         alert.showAndWait();
	    }
	sessions.clear();
	loadSessionData();
	
}
@FXML
public void answers(ActionEvent e) throws IOException {
	
	
	 if (selectedSession != null) {  
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("AnswersUserSession.fxml"));
	        Parent root = loader.load();

	   
	        UserAnswersController answerController = loader.getController();
	        answerController.setAnswers(loginController.getLoggedInMemberId(),selectedSession.getSessionId());  

	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.show();
	        loadSessionData();
	    } else {
	    	 Alert alert = new Alert(Alert.AlertType.WARNING);
	         alert.setTitle("No Session Selected");
	         alert.setHeaderText(null);
	         alert.setContentText("Please select a session to view Answers.");
	         alert.showAndWait();
	    }
	sessions.clear();
	loadSessionData();
	
}
@FXML
private void search() {
    String sessionIdInput = sessionIdTextField.getText();

    if (sessionIdInput.isEmpty()) {
        showAlert("Please enter a Project ID to search.");
        return;
    }

    try {
        int sessionId = Integer.parseInt(sessionIdInput);
        sessionTable.setItems(getSessionById(sessionId));
    } catch (NumberFormatException e) {
        showAlert("Invalid Session ID. Please enter a numeric value.");
    }
}
private ObservableList<SessionUser> getSessionById(int sessionId) {
    ObservableList<SessionUser> sessions = FXCollections.observableArrayList();

    String query = """
        SELECT s.sessionid, s.topic AS sessionname, sm.attendance, sm.answers, sm.feedback
        FROM "IMPACT Club".session s
        JOIN "IMPACT Club".sessionmember sm ON s.sessionid = sm.sessionid
        WHERE sm.memberid = ? AND s.sessionid = ?;
        """;

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setInt(1, loginController.getLoggedInMemberId());  
        stmt.setInt(2, sessionId);  

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("sessionid");
                String name = rs.getString("sessionname");
                String attendance = rs.getString("attendance");
                String answers = rs.getString("answers");
                String feedback = rs.getString("feedback");

                sessions.add(new SessionUser(id, name, attendance, answers, feedback));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return sessions;
}
private void showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Warning");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}


}