package application;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;

public class AddNewMemberController {
    @FXML
    private TextField firstNameField, middleNameField, lastNameField, ssnField, emailField, phoneField, streetField,cityField;
    @FXML
    private DatePicker birthDatePicker, startDatePicker;
    @FXML
    private RadioButton maleRadio, femaleRadio;
    @FXML
    private Button addButton, cancelButton;

   
    @FXML
    private void handleAddMember(ActionEvent event) {
        try {
            String firstName = firstNameField.getText();
            String middleName = middleNameField.getText();
            String lastName = lastNameField.getText();
            String ssn = ssnField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String street = streetField.getText();
            String city = cityField.getText();
            String gender = maleRadio.isSelected() ? "M" : "F";
            Date birthDate = Date.valueOf(birthDatePicker.getValue());
            Date startDate = Date.valueOf(startDatePicker.getValue());
            String generatedPassword = generateRandomPassword();
 DatabaseConnection database=new DatabaseConnection();
 
            try ( Connection conn = database.getConnection()) {
                String query = "INSERT INTO \"IMPACT Club\".person (first_name, middle_name, last_name, ssn, user_name, phone_number, street,city, gender, BOD, start_date, password) VALUES (?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, firstName);
                pstmt.setString(2, middleName);
                pstmt.setString(3, lastName);
                pstmt.setString(4, ssn);
                pstmt.setString(5, email);
                pstmt.setString(6, phone);
                pstmt.setString(7, street);
                pstmt.setString(8, city);
                pstmt.setString(9, gender);
                pstmt.setDate(10, birthDate); // Correctly set the date
                pstmt.setDate(11, startDate); // Correctly set the date
                pstmt.setString(12, generatedPassword);
                pstmt.executeUpdate();
                String memberQuery = "INSERT INTO \"IMPACT Club\".member ( ssn) VALUES ( ?)";
                PreparedStatement memberStmt = conn.prepareStatement(memberQuery);
                memberStmt.setString(1, ssn);
                memberStmt.executeUpdate();
            }
            
            // Send the welcome email
            sendWelcomeEmail(email, firstName, generatedPassword);

            // Confirmation message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Member added successfully and welcome email sent!");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not add member");
            alert.setContentText("An error occurred while adding the member.");
            alert.showAndWait();
        }
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    private void sendWelcomeEmail(String recipientEmail, String firstName, String password) {
    
            String Subject="Welcome to the IMPACT Club!";
            String Text="Dear " + firstName + ",\n\n"
                    + "Welcome to our club! Here are your details:\n"
                    + "email: " + recipientEmail + "\n"
                    + "Password: " + password + "\n\n"
                    + "Please log in and change your password at your earliest convenience.\n\n"
                    + "Best regards,\nThe Team";
            SendEmail send =new SendEmail(recipientEmail,Subject,Text);
           
        
    }
    @FXML
    private void handleCancel(ActionEvent event) {
        
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
