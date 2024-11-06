package application;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
public class UserPageHomeController implements Initializable{
	@FXML
	Label projectName,medale, lastpoint,points,welcome, eventsNumber,progress;
	@FXML
	private ProgressBar CPP, IPT;
 	@FXML
    private ListView<String> UPEvent; // Add Comming Tasks 
 	int pointMem;int nextTierPoint;
 	DatabaseConnection database=new DatabaseConnection();
   // @FXML
  //  private TableView<String> MembersTabel; // Specify type as String or appropriate type    
  //  @FXML
  //  private TableColumn<String, String> details, contact, tier, pointsc;
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		System.out.println("UserPageHome.fxml has been loaded!");
		pointMem=UserPageConreoller.getpoints();
		System.out.print(pointMem);
		if(pointMem%100!=0)nextTierPoint=pointMem%100;
		else nextTierPoint=100;
		whenrun();
		
	}
	void whenrun() {
		
        medale.setText("You have " + medales(pointMem) + " Medal");  // Example points passed to `medales`
        lastpoint.setText("Next Tier After " +nextTierPoint + " Points");  
        points.setText(""+pointMem);  
        eventsNumber.setText("3 New");  // Tasks in this Project
        progress.setText("Progress: " + 60 + "%");  // Placeholder progress
        double progressPercentage = (100-(nextTierPoint)) / 100.0;
        //CPP.setProgress(progressPercentage);
        IPT.setProgress(progressPercentage);
        loadCurrentProject();
        
        UPEvent.getItems().addAll("Task 1", "Task 2", "Task 3");
       // MembersTabel.getItems().add("Member Details Placeholder");    
	}
	void loadCurrentProject() {
	    String query = "SELECT projectname, description, projectstatus FROM \"IMPACT Club\".project WHERE projectstatus = 'In Progress' LIMIT 1";
	    
	    try (Connection conn = database.getConnection(); // Replace with your connection method
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