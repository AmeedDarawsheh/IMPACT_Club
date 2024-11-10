package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;

public class OverviewController {

    @FXML
    private Button projectsReportButton;

    // Method to handle the "Projects Report" button click
    @FXML
    private void handleProjectsReportButtonClick() {
        Connection connection = null;
        InputStream input = null;
        
        try {
            // Database connection setup
            DatabaseConnection data = new DatabaseConnection();
            connection = data.getConnection();

            // Load the Jasper report design from resources
            input = getClass().getResourceAsStream("/Flower_1.jrxml");
            if (input == null) {
                System.err.println("Failed to find the report file in resources.");
                return;
            }

            JasperDesign jd = JRXmlLoader.load(input);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            System.out.println("JasperReport compiled successfully!");

            // Fill the report with data from the database
            JasperPrint jasperPrint = JasperFillManager.fillReport(jr, null, connection);

            // Display the report using JasperViewer
            JasperViewer.viewReport(jasperPrint, false);
            System.out.println("Projects report displayed successfully.");

        } catch (Exception e) {
            System.err.println("Error during report generation: " + e.getMessage());
            e.printStackTrace();
            System.out.println("Failed to display the Projects report.");
        } finally {
            // Close resources
            try {
                if (input != null) input.close();
                if (connection != null) connection.close();
            } catch (Exception ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
    }
}
