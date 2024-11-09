package application;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class UserMessageSend {
    private String emailTo;
    private String emailFrom;
    private String messageContent;
    private String subject;
    String gmailPas;
    // Gmail SMTP server settings
    private final String SMTP_SERVER = "smtp.gmail.com";
    private final String SMTP_PORT = "587";

    // Constructor
    public UserMessageSend(String emailTo, String emailFrom, String messageContent, String subject) {
        this.emailTo = emailTo;
        this.emailFrom = emailFrom;
        this.messageContent = messageContent;
        this.subject = subject;
        //if(emailFrom=="ce.ameed@gmail.com")
        gmailPas="mipm bvcr jumm bxcf";
    }

    // Getters
    public String getEmailTo() {
        return emailTo;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getSubject() {
        return subject;
    }

    // Method to send email using Gmail SMTP
    public boolean sendEmail() {
    
        // Set up properties for Gmail SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_SERVER);
        properties.put("mail.smtp.port", SMTP_PORT);

        // Authenticate with Gmail
        Session session = Session.getInstance(properties, new Authenticator() {
        	
            @Override
            
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, gmailPas);
            }
        });

        try {
            // Create email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailFrom));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
            message.setSubject(subject);
            message.setText(messageContent);

            // Send email
            Transport.send(message);
            System.out.println("Email sent successfully to " + emailTo);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email to " + emailTo);
            return false;
        }
    }

   
}
