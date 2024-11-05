package application;
import java.net.URL;
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
   // @FXML
  //  private TableView<String> MembersTabel; // Specify type as String or appropriate type    
  //  @FXML
  //  private TableColumn<String, String> details, contact, tier, pointsc;
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		System.out.println("UserPageHome.fxml has been loaded!");
		whenrun();	
	}
	void whenrun() {
		projectName.setText("Sample Project");  // Replace with actual project name if available
        medale.setText("You have " + medales(10) + " Medal");  // Example points passed to `medales`
        lastpoint.setText("Next Tier After " + 100 + " Points");  // Placeholder points
        points.setText("9999");  // Replace with SQL result points
        eventsNumber.setText("3 New");  // Tasks in this Project
        progress.setText("Progress: " + 60 + "%");  // Placeholder progress
     
        CPP.setProgress(0.1); //Percentage
        IPT.setProgress(0.1);
        
        UPEvent.getItems().addAll("Task 1", "Task 2", "Task 3");
       // MembersTabel.getItems().add("Member Details Placeholder");    
	}
    private String medales(int points) {
        String tier="";
        int pointscs = points % 5;  // This calculates the remainder
        switch (pointscs) {
            case 0:
                tier = "Bronze";
                break;
            case 1:
                tier = "Gold";
                break;
            case 2:
                tier = "Diamond";
                break;
            default:
                tier = "Armoush";
                break;
        }
        return tier;
    }	
}