package application;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.scene.control.Alert;

public class SendEmail {
	String toEmail;
	final String password = "ntsy xkaf luvw lqtq";
	String fromEmail="impactclubalnaqoura@gmail.com";
	public  SendEmail(String toEmail ,String subject, String messageContent) {
	    // Email to send to
	    this.toEmail=toEmail;
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

	        
	        String fullMessage = "From: " + fromEmail+ "\n\n" + messageContent;
	        message.setText(fullMessage);

	        Transport.send(message);
	       
	    } catch (MessagingException e) {
	       // e.printStackTrace();
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Email Status");
	        alert.setHeaderText("Error");
	        alert.setContentText("Failed to send the email.\n Email address is wrong.");
	        alert.showAndWait();
	    }
	}
}
