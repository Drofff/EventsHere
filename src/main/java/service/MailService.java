package service;

import repository.ProfileRepository;
import repository.UserRepository;
import entity.Profile;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MailService implements Serializable {

    private static MailService mailService;

    public static String BASE_URL;

    private static Session session;

    private MailService() {}

    public static MailService getInstance() {
        if (mailService == null) {
            mailService = new MailService();
            init();
        }
        return mailService;
    }

    private void sendMimeMessage(String email, String topic, String message) throws MessagingException {

        MimeMessage mimeMessage = new MimeMessage(session);

        mimeMessage.setSubject(topic);

        mimeMessage.setFrom(new InternetAddress("events@here.com"));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(message, "text/html");

        MimeMultipart mimeMultipart = new MimeMultipart();
        mimeMultipart.addBodyPart(mimeBodyPart);

        mimeMessage.setContent(mimeMultipart);

        Transport.send(mimeMessage);

    }

    public void sendNotification(Long profileId, Long eventId, HttpSession httpSession) {

         ProfileRepository profileRepository = ProfileRepository.getInstance(httpSession);
         UserRepository userRepository = UserRepository.getInstance(httpSession);

         Profile profile = profileRepository.findById(profileId);
         List<Profile> subscribers = profileRepository.getSubscribers(profileId);

         String message = "Hey, there! Do you know that " + profile.getFirstName() + " " + profile.getLastName() + " started new event?<br/>Link here <a href='" + BASE_URL + "/event?id=" + eventId + "'>View event</a>";

         for (Profile sub : subscribers) {

             if (sub.getNotifyMe()) {

                 try {

                     sendMimeMessage(userRepository.findById(sub.getUserId()), "New Event Here!", message);

                 } catch (Exception e) {
                     e.printStackTrace();
                 }

             }

         }

    }

    public void sendBlockedWarning(String email, String reason) throws MessagingException {

        String message = "Dear, User! You were blocked by administrator because of: " + reason;

        sendMimeMessage(email, "Blocked account", message);

    }

    public void sendRecoveryToken(String email, String token) throws MessagingException {

        String link = BASE_URL + "/forgotPassword?token=" + token;

        String message = "Dear, User! Thank you for using our application. To recover your account password, please press button below. If you don't want to do it - just ignore this message.<br/><br/><a href='" + link + "' style='margin-left:30%;'>Recover password</a>";

        sendMimeMessage(email, "Password recovery", message);

    }

    public void sendActivationToken(String email, String token) throws MessagingException {

        String link = BASE_URL + "/registration?token=" + token;

        String message = "Welcome to Events Here! To activate your account, please follow link:<br/><br/><a href='" + link + "' style='margin-left:30%;'>Activate account</a>";

        sendMimeMessage(email, "Account activation", message);

    }

    private static void init() {

        Map<String, String> connectionProperties = PropertiesService.getInstance().getMailProperties();

        BASE_URL = connectionProperties.get("base_url");

        Properties properties = new Properties();

        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.host", connectionProperties.get("host"));
        properties.put("mail.smtp.port", connectionProperties.get("port"));

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(connectionProperties.get("username"), connectionProperties.get("password"));
            }
        });
    }

}
