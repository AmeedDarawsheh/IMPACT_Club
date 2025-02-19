package application;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.sql.DriverManager;

public class ReportViewer {
    public static void main(String[] args) {
        try {
       
            String url = "jdbc:postgresql://localhost:5432/your_database"; 
            String user = "your_username"; 
            String password = "your_password"; 
            Connection connection = DriverManager.getConnection(url, user, password);

            
            String reportPath = "path/to/your_report.jasper"; 

            // Compile the report if you only have a .jrxml file
            // JasperReport jasperReport = JasperCompileManager.compileReport("path/to/your_report.jrxml");

          
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, null, connection);

         
            JasperViewer.viewReport(jasperPrint, false);

            System.out.println("Report generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
