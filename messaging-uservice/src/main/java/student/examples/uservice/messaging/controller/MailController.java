package student.examples.uservice.messaging.controller;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.web.bind.annotation.*;
import student.examples.uservice.messaging.dto.Email;

import java.util.Properties;

@RestController
@RequestMapping("/email")
public class MailController {
    @PostMapping("/send")
    public String signup(@RequestBody Email email) {
        if (!sendEmail(email.getEmail(), email.getToken()))
            return "FAIL";
        return "OK";
    }


    private boolean sendEmail(String email, String token) {
        if(token.isEmpty()){
            return false;
        }

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.tls.trust", "smtp.gmail.com");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("gilat.godzila.game@gmail.com", "fzdn bejx uwsq xgyt");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("gilat.godzila.game@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("victor0198@gmail.com"));// TODO: replace with email));
            message.setSubject("Mail Subject");

            String msg = "<div><h1>It's not scam, click to play!</h1><a href=\"http://localhost:8444/auth/activation?token="+token+"\"><h1>Start Game</h1></a>";

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            System.out.println("Trying to email with send token:" + token);
            Transport.send(message);
            System.out.println("Email sent");
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
