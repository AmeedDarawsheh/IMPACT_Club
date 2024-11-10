package application;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.sql.DriverManager;

public class ReportViewer {
    public static void main(String[] args) {
        try {
            // Set up database connection
            String url = "jdbc:postgresql://localhost:5432/your_database"; // replace with your database URL
            String user = "your_username"; // replace with your database username
            String password = "your_password"; // replace with your database password
            Connection connection = DriverManager.getConnection(url, user, password);

            // Path to the compiled Jasper report file (.jasper)
            String reportPath = "path/to/your_report.jasper"; // replace with the actual path to your .jasper file

            // Compile the report if you only have a .jrxml file
            // JasperReport jasperReport = JasperCompileManager.compileReport("path/to/your_report.jrxml");

            // Fill the report with data from the database connection
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, null, connection);

            // View the report
            JasperViewer.viewReport(jasperPrint, false);

            System.out.println("Report generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
