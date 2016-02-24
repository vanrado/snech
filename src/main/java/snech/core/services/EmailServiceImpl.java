package snech.core.services;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;
import snech.core.types.User;

/**
 *
 * @author vanrado
 */
@Service
public class EmailServiceImpl implements IEmailService {

    
    @Override
    public void sendInfoForTechnicianAssign(User user, long issueId) {
        String from = "admin@azet.sk";

        final String username = "yourEmail";
        final String password = "yourPassword";

        String host = "smtp.gmail.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail() != null ? user.getEmail() : ""));
            message.setSubject("Info SNECH: Bola vám pridelená požiadavka s id #" + issueId);
            message.setText("Dobrý deň " + user.getFirstName() + " " + user.getLastName() + 
                    ", \nbola Vám pridelená požiadavka s id #" + issueId +". Po prihlásení do systému SNECH ju nájdete pridelenú v obrazovke Moje úlohy. \nOdoslané so systému SNECH");

            Transport.send(message);
            System.out.println("Email bol odoslany");
        } catch (MessagingException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void sendInfoForMemberAssign(User user, long issueId) {
        String from = "admin@azet.sk";

        final String username = "radovan.racak";
        final String password = "ofi2o12r";

        String host = "smtp.gmail.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail() != null ? user.getEmail() : ""));
            message.setSubject("Info SNECH: Vaša požiadavka bola pridelená");
            message.setText("Dobrý deň, " + user.getFirstName() + " " + user.getLastName() + "\n\nvami zadanej požiadavke bol pridelený technik na riešenie. \n\nOdoslané zo systému SNECH");
//...konfiguracia portov, hostu...
            Transport.send(message);
        } catch (MessagingException ex) {
            Logger.getLogger(DatabaseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
