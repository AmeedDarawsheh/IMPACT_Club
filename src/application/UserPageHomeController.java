package application;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
public class UserPageHomeController implements Initializable{
	@FXML
	Label projectName,medale, lastpoint,points,welcome, eventsNumber,progress;
	@FXML
	private ProgressBar CPP, IPT;
 	@FXML
    private ListView<String> UPEvent; 
 	int pointMem;int nextTierPoint;
 	DatabaseConnection database=new DatabaseConnection();
 	Connection con=database.getConnection();
 	@FXML
    private TableView<Member> memberTable;
    @FXML
    private TableColumn<Member, String> nameColumn;
    @FXML
    private TableColumn<Member, String> phoneColumn;
    @FXML
    private TableColumn<Member, String> emailColumn;
    @FXML
    private TableColumn<Member, Integer> pointsColumn;
    ObservableList<Member> members;
    String selectedEmail;
	public void initialize(URL arg0, ResourceBundle arg1) {
	
		
		System.out.println("UserPageHome.fxml has been loaded!");
		pointMem=UserPageConreoller.getpoints();
		System.out.print(pointMem);
		if(pointMem%100!=0)nextTierPoint=pointMem%100;
		else nextTierPoint=100;
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
        memberTable.setRowFactory(tv -> {
            TableRow<Member> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Member selectedMember = row.getItem();
                    selectedEmail = selectedMember.getEmail(); 
                }
            });
            return row;
        });
		whenrun();
		loadDatabase();
		
	}

@FXML
public void sendMailToColleague(ActionEvent event) {
    if (selectedEmail != null) {

        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Send Message");
        inputDialog.setHeaderText("Enter the message content to send to " + selectedEmail);
        inputDialog.setContentText("Message:");

        Optional<String> result = inputDialog.showAndWait();
        result.ifPresent(messageContent -> {
            if (!messageContent.isEmpty()) {
            
                UserMessageSend message = new UserMessageSend(selectedEmail, getUsernameByMemberId(loginController.getLoggedInMemberId()), messageContent,"Contact with colleague in IMPACT Club");
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
    } else {
        showAlert(AlertType.WARNING, "No Selection", "Please select a colleague to send the email.");
    }
}
public String getUsernameByMemberId(int memberId) {
    String userName = null;
    String sql = "SELECT p.user_name " +
                 "FROM \"IMPACT Club\".person p " +
                 "JOIN \"IMPACT Club\".member m ON p.ssn = m.ssn " +
                 "WHERE m.memberid = ?";

    try (
         PreparedStatement pstmt = con.prepareStatement(sql)) {

        pstmt.setInt(1, memberId);

       
        ResultSet rs = pstmt.executeQuery();

       
        if (rs.next()) {
           
            userName = rs.getString("user_name");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return userName;
}
	private void showAlert(AlertType alertType, String title, String message) {
	    Alert alert = new Alert(alertType);
	    alert.setTitle(title);
	    alert.setHeaderText(null); // No header
	    alert.setContentText(message);
	    alert.showAndWait();
	}

	private  void loadDatabase() {
        ObservableList<Member> members = FXCollections.observableArrayList();

        String query = """
            SELECT p.first_name || ' ' || p.last_name AS name, p.phone_number, p.user_name AS email, m.points
            FROM "IMPACT Club".person p
            JOIN "IMPACT Club".member m ON p.ssn = m.ssn
            ORDER BY m.points DESC;
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                String phone = rs.getString("phone_number");
                String email = rs.getString("email");
                int points = rs.getInt("points");

                members.add(new Member(name, phone, email, points));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

      memberTable.setItems(members);
    }

	void whenrun() {
		
        medale.setText("You have " + medales(pointMem) + " Medal"); 
        lastpoint.setText("Next Tier After " +nextTierPoint + " Points");  
        points.setText(""+pointMem);  
        eventsNumber.setText("3 New");  
        progress.setText("Progress: " + 60 + "%");  
        double progressPercentage = (100-(nextTierPoint)) / 100.0;
        CPP.setProgress(0.6);
        IPT.setProgress(progressPercentage);
        loadCurrentProject();
        
        UPEvent.getItems().addAll("Task 1", "Task 2", "Task 3");
       // MembersTabel.getItems().add("Member Details Placeholder");    
	}
	void loadCurrentProject() {
	    String query = "SELECT projectname, description, projectstatus FROM \"IMPACT Club\".project WHERE projectstatus = 'In Progress' LIMIT 1";
	    
	    try (Connection conn = database.getConnection(); 
	         PreparedStatement pstmt = conn.prepareStatement(query)) {
	         
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            String projectNameText = rs.getString("projectname");
	            String projectDescription = rs.getString("description");
	            projectName.setText(projectNameText);
	            //progress.setText("Progress: " + projectDescription); // Assuming you want to display the description here
	        } else {
	            projectName.setText("No projects in progress");
	            progress.setText("");
	        }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

    private String medales(int points) {
        String tier="";
        int pointscs = points / 100;  
        switch (pointscs) {
            case 0:
                tier = "Bronze";
                break;
            case 1:
                tier = "Silver";
                break;
            case 2:
                tier = "Gold";
                break;
            case 3:
                tier = "Platinum";
                
                break;
            case 4:
                tier = "Diamond";
                break;
            case 5:
                tier = "Silver";
                break;
            case 6:
                tier = "Master";
                break;
            case 7:
                tier = "GrandMaster";
                
                break; 
            case 8:
                tier = "Legendary";
                break;
            case 9:
                tier = "Mythicr";
                break;
           
        }
        return tier;
    }	
}