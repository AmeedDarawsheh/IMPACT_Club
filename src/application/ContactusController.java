package application;

import java.io.IOException;
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

public class ContactusController {
	public Stage stage ;
	private Scene scene;
	public Parent root;
@FXML
private TextField emailField;
@FXML
private TextField subjectField;
@FXML
private TextArea commentArea;

public void sendEmail(String subject, String messageContent) {
    // Email to send to
    final String toEmail = "";
    
    final String password = "gdlw qrlm xalu yqho"; // Your password
String fromEmail = emailField.getText();
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
        message.setSubject(subject);

        
        String fullMessage = "From: " + emailField.getText() + "\n\n" + messageContent;
        message.setText(fullMessage);

        Transport.send(message);
       
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Email Status");
        alert.setHeaderText(null);
        alert.setContentText("Email sent successfully! \n Thank you for contact us we will reply very soon");
        alert.showAndWait();
    } catch (MessagingException e) {
       // e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Email Status");
        alert.setHeaderText("Error");
        alert.setContentText("Failed to send the email.\n Email address is wrong.");
        alert.showAndWait();
    }
}

@FXML
private void handleSubmitButtonAction() {
    String subject = subjectField.getText();
    String comment = commentArea.getText();

    if (!emailField.getText().isEmpty() && !subject.isEmpty() && !comment.isEmpty()) {
        sendEmail(subject, comment);
    } else {
        System.out.println("Please fill out all fields.");
    }
}
public void Back(ActionEvent e) throws IOException {
	root = FXMLLoader.load(getClass().getResource("Login.fxml"));
	stage = (Stage)((Node)e.getSource()).getScene().getWindow();
	scene = new Scene(root);
	stage.setScene(scene);
	stage.show();
	stage.setTitle("Login");
	stage.centerOnScreen();
	stage.setResizable(false);
}

}
