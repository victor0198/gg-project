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
    @PostMapping("/activate")
    public String signup(@RequestBody Email email) {
        if(email.getToken().isEmpty()){
            return "FAIL";
        }

        String msg = "<div><h1>It's not a scam, click to play!</h1><a href=\"http://localhost:8444/auth/activation?token="+email.getToken()+"\"><h1>Start Game</h1></a>";

        if (!sendEmail(email.getEmail(), msg))
            return "FAIL";
        return "OK";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestBody Email email) {
        if(email.getToken().isEmpty()){
            return "FAIL";
        }

        String msg = "<div><h1>Careful! You're deleting your account.</h1><a href=\"http://localhost:8444/auth/withdrawal/confirmation?token="+email.getToken()+"\"><h1>Confirm</h1></a>";

        if (!sendEmail(email.getEmail(), msg))
            return "FAIL";
        return "OK";
    }


    private boolean sendEmail(String email, String msg) {

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



            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email sent");
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
