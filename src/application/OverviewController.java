package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class OverviewController implements Initializable{
	 @FXML
	    private BarChart<String, Number> projectInvolvementChart;
	 @FXML
	    private LineChart<String, Number> memberPointsChart;
	 @FXML
	 private Label projectsLabel,membersLabel,plansLabel,sessionsLabel;
	    @FXML
	    private CategoryAxis xAxis;
	    @FXML
	    private Label recentMember1Label, completedProjectsLabel;
	    
	    DatabaseConnection data = new DatabaseConnection();
       Connection  connection = data.getConnection();
	    

    @FXML
    private Button projectsReportButton;
    @FXML

    private void loadProjectInvolvementData() {
        Connection connection = null;

        try {
           
            DatabaseConnection data = new DatabaseConnection();
            connection = data.getConnection();
         


          
            String sql = "SELECT projectname, AVG(projectinvolvementlevel) as avg_involvement " +
                         "FROM \"IMPACT Club\".memberproject INNER JOIN \"IMPACT Club\".project ON memberproject.projectid = project.projectid " +
                         "GROUP BY projectname";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            
            projectInvolvementChart.getData().clear();

       
            XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
            dataSeries.setName("Involvement Level");

           
            while (rs.next()) {
                String projectName = rs.getString("projectname");
                double avgInvolvement = rs.getDouble("avg_involvement");
                dataSeries.getData().add(new XYChart.Data<>(projectName, avgInvolvement));
            }

        
            projectInvolvementChart.getData().add(dataSeries);
           


        } catch (Exception e) {
            System.err.println("Error loading data for chart: " + e.getMessage());
            e.printStackTrace();
        } finally {
         
            try {
                if (connection != null) connection.close();
            } catch (Exception ex) {
                System.err.println("Error closing database connection: " + ex.getMessage());
            }
        }
    }
    private void loadMemberCount() {
        Connection connection = null;

        try {
           
            DatabaseConnection data = new DatabaseConnection();
            connection = data.getConnection();

            
            String sql = "SELECT COUNT(*) AS total_members FROM \"IMPACT Club\".member";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            
            if (rs.next()) {
                int totalMembers = rs.getInt("total_members");
                membersLabel.setText(String.valueOf(totalMembers));
            }

        } catch (Exception e) {
            System.err.println("Error loading member count: " + e.getMessage());
            e.printStackTrace();
        } finally {
           
            try {
                if (connection != null) connection.close();
            } catch (Exception ex) {
                System.err.println("Error closing database connection: " + ex.getMessage());
            }
        }
    }
    public void showCompletedProjects() {
        StringBuilder completedProjects = new StringBuilder("");
        
        String query = "SELECT projectname FROM \"IMPACT Club\".project WHERE projectstatus = 'Completed' Limit 1";

        try (
               
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String projectName = rs.getString("projectname");
                
                completedProjects.append(projectName);
            }

           
            if (completedProjects.length() > 0) {
                completedProjects.setLength(completedProjects.length() - 2);
            }

            completedProjectsLabel.setText(completedProjects.toString());

        } catch (Exception e) {
            e.printStackTrace();
            completedProjectsLabel.setText("Error loading completed projects");
        }
    }
    private void loadMemberPointsData() {
        Connection connection = null;

        try {
          
            DatabaseConnection data = new DatabaseConnection();
            connection = data.getConnection();

          
            String sql = "SELECT p.first_name AS first_name, m.points " +
                         "FROM \"IMPACT Club\".member m " +
                         "JOIN \"IMPACT Club\".person p ON m.ssn = p.ssn " +
                         "ORDER BY m.memberid ASC";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            memberPointsChart.getData().clear();

            
            XYChart.Series<String, Number> pointsSeries = new XYChart.Series<>();
            pointsSeries.setName("Member Points");

           
            while (rs.next()) {
                String firstName = rs.getString("first_name");
                int points = rs.getInt("points");
                pointsSeries.getData().add(new XYChart.Data<>(firstName, points));
            }

          
            memberPointsChart.getData().add(pointsSeries);

        } catch (Exception e) {
            System.err.println("Error loading data for chart: " + e.getMessage());
            e.printStackTrace();
        } finally {
   
            try {
                if (connection != null) connection.close();
            } catch (Exception ex) {
                System.err.println("Error closing database connection: " + ex.getMessage());
            }
        }
    }

 
    private void loadPlanCount() {
        Connection connection = null;

        try {
      
            DatabaseConnection data = new DatabaseConnection();
            connection = data.getConnection();

            
            String sql = "SELECT COUNT(*) AS total_plans FROM \"IMPACT Club\".plan";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);


            if (rs.next()) {
                int totalPlans = rs.getInt("total_plans");
                plansLabel.setText(String.valueOf(totalPlans));
            }

        } catch (Exception e) {
            System.err.println("Error loading plan count: " + e.getMessage());
            e.printStackTrace();
        } finally {
           
            try {
                if (connection != null) connection.close();
            } catch (Exception ex) {
                System.err.println("Error closing database connection: " + ex.getMessage());
            }
        }
    }
    private void loadProjectCount() {
        Connection connection = null;

        try {
    
            DatabaseConnection data = new DatabaseConnection();
            connection = data.getConnection();

   
            String sql = "SELECT COUNT(*) AS total_projects FROM \"IMPACT Club\".project";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

        
            if (rs.next()) {
                int totalProjects = rs.getInt("total_projects");
                projectsLabel.setText(String.valueOf(totalProjects));
            }

        } catch (Exception e) {
            System.err.println("Error loading project count: " + e.getMessage());
            e.printStackTrace();
        } finally {
    
            try {
                if (connection != null) connection.close();
            } catch (Exception ex) {
                System.err.println("Error closing database connection: " + ex.getMessage());
            }
        }
    }
    private void loadSessionCount() {
        Connection connection = null;

        try {
      
            DatabaseConnection data = new DatabaseConnection();
            connection = data.getConnection();


            String sql = "SELECT COUNT(*) AS total_sessions FROM \"IMPACT Club\".session";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                int totalSessions = rs.getInt("total_sessions");
                sessionsLabel.setText(String.valueOf(totalSessions));
            }

        } catch (Exception e) {
            System.err.println("Error loading session count: " + e.getMessage());
            e.printStackTrace();
        } finally {
         
            try {
                if (connection != null) connection.close();
            } catch (Exception ex) {
                System.err.println("Error closing database connection: " + ex.getMessage());
            }
        }
    }
    private void loadRecentMembers() {
        Connection connection = null;

        try {
      
            DatabaseConnection data = new DatabaseConnection();
            connection = data.getConnection();

                     String sql = "SELECT first_name, middle_name, last_name FROM \"IMPACT Club\".person " +
                         "ORDER BY start_date DESC LIMIT 1";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

          
            Label[] recentMemberLabels = { recentMember1Label};
            int index = 0;

           
            while (rs.next() && index < recentMemberLabels.length) {
                String fullName = rs.getString("first_name") + " " +
                                  rs.getString("middle_name") + " " +
                                  rs.getString("last_name");
                recentMemberLabels[index].setText(fullName);
                index++;
            }

        } catch (Exception e) {
            System.err.println("Error loading recent members: " + e.getMessage());
            e.printStackTrace();
        } finally {
           
            try {
                if (connection != null) connection.close();
            } catch (Exception ex) {
                System.err.println("Error closing database connection: " + ex.getMessage());
            }
        }
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		loadProjectInvolvementData();
		loadMemberPointsData();
		loadMemberCount();
		loadProjectCount();
		 loadPlanCount();
		 loadSessionCount();
		 loadRecentMembers();
		 showCompletedProjects();
	}
}
