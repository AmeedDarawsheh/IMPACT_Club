package application;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
public class UserPageProjectController implements Initializable {	
	@FXML
    private TableView<ProjectField> projectTable;
    @FXML
    private TableColumn<ProjectField, Integer> projectIdColumn;
    @FXML
    private TableColumn<ProjectField, String> projectNameColumn;
    @FXML
    private TableColumn<ProjectField, String> statusColumn;
    @FXML
    private TableColumn<ProjectField, String> roleColumn;
    @FXML
    private TableColumn<ProjectField, String> feedbackColumn;
    @FXML
    private TableColumn<ProjectField, Integer> involvementLevelColumn;
    private ObservableList<ProjectField> projects ;

    @FXML 
    private TextField projectIdTextField;
    private ProjectField selectedProject;
    DatabaseConnection data =new DatabaseConnection();
    Connection con=data.getConnection();
    
	@FXML
	public void addProject(ActionEvent e) throws IOException {	
			
		Parent root = FXMLLoader.load(getClass().getResource("UserAddProject.fxml"));
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();	
	}
	@FXML
	public void sendMailToLeaders(ActionEvent event) {
	    
	     
	        TextInputDialog inputDialog = new TextInputDialog();
	        inputDialog.setTitle("Send Message");
	        inputDialog.setHeaderText("Enter the message content to send to " + "IMPACT Club Gmail ");
	        inputDialog.setContentText("Message:");

	        Optional<String> result = inputDialog.showAndWait();
	        result.ifPresent(messageContent -> {
	            if (!messageContent.isEmpty()) {
	               
	                UserMessageSend message = new UserMessageSend("", getUsernameByMemberId(loginController.getLoggedInMemberId()), messageContent,"Contact with Leader in IMPACT Club");
	                boolean success = message.sendEmail();

	                if (success) {
	                    showAlert(AlertType.INFORMATION, "Email Sent", "The message has been sent successfully.");
	                } else {
	                    showAlert(AlertType.ERROR, "Email Failed", "Failed to send the message.");
	                }
	            } else {
	                showAlert(AlertType.WARNING, "Empty Message", "The message content cannot be empty.");
	            }
	        });
	    
	}
	private void showAlert(AlertType alertType, String title, String message) {
	    Alert alert = new Alert(alertType);
	    alert.setTitle(title);
	    alert.setHeaderText(null); // No header
	    alert.setContentText(message);
	    alert.showAndWait();
	}

	public String getUsernameByMemberId(int memberId) {
	    String userName = null;
	    String sql = "SELECT p.user_name " +
	                 "FROM \"IMPACT Club\".person p " +
	                 "JOIN \"IMPACT Club\".member m ON p.ssn = m.ssn " +
	                 "WHERE m.memberid = ?";

	    try (
	         PreparedStatement pstmt = con.prepareStatement(sql)) {

	        // Set the memberId parameter in the query
	        pstmt.setInt(1, memberId);

	        // Execute the query
	        ResultSet rs = pstmt.executeQuery();

	        // Check if the result contains a row
	        if (rs.next()) {
	            // Get the user_name from the result
	            userName = rs.getString("user_name");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return userName;
	}
	@FXML
	public void feedBack(ActionEvent e) throws IOException {
		
		
		 if (selectedProject != null) {  // Ensure a project is selected
		        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserFeedBack.fxml"));
		        Parent root = loader.load();

		        // Get the controller of the feedback scene
		        UserFeedBackController feedbackController = loader.getController();
		        feedbackController.setFeedback(loginController.getLoggedInMemberId(),selectedProject.getProjectId(),true);  // Pass the selected project

		        Stage stage = new Stage();
		        stage.setScene(new Scene(root));
		        stage.show();
		    } else {
		    	 Alert alert = new Alert(Alert.AlertType.WARNING);
		         alert.setTitle("No Project Selected");
		         alert.setHeaderText(null);
		         alert.setContentText("Please select a project to view feedback.");
		         alert.showAndWait();
		    }
		loadData();
		
	}
	@FXML
	public void sendMassage(ActionEvent e) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("UserProjectSendMassage.fxml"));
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
		
		
	}
	
	@FXML
	public void reportE(ActionEvent e) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Report");
		alert.setHeaderText("You're about to Send Report Via Email!");
		alert.setContentText("Are you sure About it ?");	
		if(alert.showAndWait().get() == ButtonType.OK){			
			//jasperreport send via email from club to user about projects 
			
			//SQL	
		}	
	}	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	projects = FXCollections.observableArrayList();
		  projectIdColumn.setCellValueFactory(new PropertyValueFactory<>("projectId"));
	        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
	        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
	        roleColumn.setCellValueFactory(new PropertyValueFactory<>("roleInProject"));
	        feedbackColumn.setCellValueFactory(new PropertyValueFactory<>("projectFeedback"));
	        involvementLevelColumn.setCellValueFactory(new PropertyValueFactory<>("projectInvolvementLevel"));
	        loadData();
	        projectTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	            selectedProject = newSelection;});
	}
	private void loadData() {
		projects.clear();
		String query = """
	            SELECT p.projectid, p.projectname, p.projectstatus, mp.roleinproject, mp.projectfeedback, mp.projectinvolvementlevel
	            FROM "IMPACT Club".project p
	            JOIN "IMPACT Club".memberproject mp ON p.projectid = mp.projectid
	            WHERE mp.memberid = ?
	            ORDER BY p.projectid ASC;
	            """;

	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(query)) {

	            stmt.setInt(1, loginController.getLoggedInMemberId());  // Set the member ID for filtering

	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    int projectId = rs.getInt("projectid");
	                    String projectName = rs.getString("projectname");
	                    String status = rs.getString("projectstatus");
	                    String roleInProject = rs.getString("roleinproject");
	                    String projectFeedback = rs.getString("projectfeedback");
	                    int involvementLevel = rs.getInt("projectinvolvementlevel");

	                    projects.add(new ProjectField(projectId, projectName, status, roleInProject, projectFeedback, involvementLevel));
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
      projectTable.setItems(projects);
	      
	    }
    @FXML
    private void search() {
        String projectIdInput = projectIdTextField.getText();

        if (projectIdInput.isEmpty()) {
            showAlert("Please enter a Project ID to search.");
            return;
        }

        try {
            int projectId = Integer.parseInt(projectIdInput);
            projectTable.setItems(getProjectById(projectId));
        } catch (NumberFormatException e) {
            showAlert("Invalid Project ID. Please enter a numeric value.");
        }
    }

    private ObservableList<ProjectField> getProjectById(int projectId) {
        ObservableList<ProjectField> projects = FXCollections.observableArrayList();

        String query = """
            SELECT p.projectid, p.projectname, p.projectstatus, mp.roleinproject, mp.projectfeedback, mp.projectinvolvementlevel
            FROM "IMPACT Club".project p
            JOIN "IMPACT Club".memberproject mp ON p.projectid = mp.projectid
            WHERE mp.memberid = ? AND p.projectid = ?;
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, loginController.getLoggedInMemberId());
            stmt.setInt(2, projectId);       

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("projectid");
                    String name = rs.getString("projectname");
                    String status = rs.getString("projectstatus");
                    String role = rs.getString("roleinproject");
                    String feedback = rs.getString("projectfeedback");
                    int involvementLevel = rs.getInt("projectinvolvementlevel");

                    projects.add(new ProjectField(id, name, status, role, feedback, involvementLevel));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }
    @FXML
    private void reload(MouseEvent event) {
       loadData();
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}