package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Session;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class ForgotPasswordController {

    @FXML
    private TextField emailField;
    int verificationCode=0;
    String userEmail;
    @FXML
    private void handleSendCode(ActionEvent event) {
         userEmail = emailField.getText();
        System.out.println("Checking email: " + userEmail);

        if (userEmail.isEmpty()) {
            showAlert("Error", "Please enter your email.", Alert.AlertType.ERROR);
            return;
        }
        DatabaseConnection databaseConnection = new DatabaseConnection();
        // Generate a verification code
         verificationCode = (int) (Math.random() * 9000) + 1000; // 4-digit code
        String query = "SELECT user_name FROM \"IMPACT Club\".person WHERE user_name = ?";

        try (Connection connection = databaseConnection.getConnection();
        		PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userEmail);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Email exists, proceed to send the verification code
                sendVerificationEmail(userEmail,verificationCode);
                showAlert("Success", "Verification code sent to your email.", Alert.AlertType.INFORMATION);
                openVerificationWindow(event);
            } else {
                // Email not found
                showAlert("Error", "The email is not registered.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while checking the email.", Alert.AlertType.ERROR);
        }
       
    }

    private boolean sendVerificationEmail(String toEmail, int code) {
        final String fromEmail = "impactclubalnaqoura@gmail.com"; // Replace with your Gmail
        final String password = "ntsy xkaf luvw lqtq"; // Replace with your generated App Password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Password Reset Verification Code");
            message.setText("Your verification code is: " + code);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void openVerificationWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("verificationcode.fxml"));
            Parent root = loader.load();

            // Pass the verification code to the VerificationController
            VerificationController verificationController = loader.getController();
            verificationController.setVerificationCode(verificationCode,userEmail);

            Stage stage = new Stage();
            stage.setTitle("Verification");
            stage.setScene(new Scene(root));
            stage.show();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
