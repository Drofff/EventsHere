package service;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.Serializable;
import java.util.Properties;

public class MailService implements Serializable {

    private static MailService mailService;

    private static final String SMTP_USERNAME = "8b57e990161d0a";

    private static final String SMTP_PASSWORD = "e0c6c44eeead3e";

    private static final String SMTP_HOST = "smtp.mailtrap.io";

    private static final String SMTP_PORT = "465";

    private static Session session;

    private MailService() {}

    public static MailService getInstance() {
        if (mailService == null) {
            mailService = new MailService();
            init();
        }
        return mailService;
    }

    private MimeMessage getMimeMessage(String email, String topic, String message) throws MessagingException {

        MimeMessage mimeMessage = new MimeMessage(session);

        mimeMessage.setSubject(topic);

        mimeMessage.setFrom(new InternetAddress("events@here.com"));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(message, "text/html");

        MimeMultipart mimeMultipart = new MimeMultipart();
        mimeMultipart.addBodyPart(mimeBodyPart);

        mimeMessage.setContent(mimeMultipart);

        return mimeMessage;

    }

    public void sendBlockedWarning(String email, String reason) throws MessagingException {

        String message = "Dear, User! You were blocked by administrator because of: " + reason;

        MimeMessage mimeMessage = getMimeMessage(email, "Blocked account", message);

        Transport.send(mimeMessage);

    }

    public void sendRecoveryToken(String email, String token) throws MessagingException {

        String link = "http://localhost:8080/EventsHere/forgotPassword?token=" + token;

        String message = "Dear, User! Thank you for using our application. To recover your account password, please press button below. If you don't want to do it - just ignore this message.<br/><br/><a href='" + link + "' style='margin-left:30%;'>Recover password</a>";

        MimeMessage mimeMessage = getMimeMessage(email, "Password recovery", message);

        Transport.send(mimeMessage);

    }

    public void sendActivationToken(String email, String token) throws MessagingException {

        String link = "http://localhost:8080/EventsHere/registration?token=" + token;

        String message = "Welcome to Events Here! To activate your account, please follow link:<br/><br/><a href='" + link + "' style='margin-left:30%;'>Activate account</a>";

        MimeMessage mimeMessage = getMimeMessage(email, "Account activation", message);

        Transport.send(mimeMessage);

    }

    private static void init() {

        Properties properties = new Properties();

        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });
    }

}
