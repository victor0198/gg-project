package student.examples.uservice.business.grpc;

import com.example.grpc.AuthService;
import com.example.grpc.SignIpServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

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

//import java.util.UUID;
import net.devh.boot.grpc.server.service.GrpcService;
import student.examples.uservice.business.domain.entity.User;
import student.examples.uservice.business.service.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;

import student.examples.uservice.business.util.TokenManager;

@GrpcService
public class AuthServiceImpl extends SignIpServiceGrpc.SignIpServiceImplBase {

    @Autowired
    UserService userService;

    @Override
    public void signing(AuthService.UserSignUpRequest request, StreamObserver<AuthService.UserSignUpResponse> responseObserver) {
        UUID uuid = UUID.randomUUID();

        String ggToken = TokenManager.buildGGToken(uuid, request.getEmail());

        boolean sent = sendEmail(request.getEmail(), ggToken);
        if(!sent){
            AuthService.UserSignUpResponse reply = AuthService.UserSignUpResponse.newBuilder()
                    .setStatus("failed")
                    .build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }

        User user = new User(uuid, request.getEmail(), request.getUsername(), request.getPassword(), ggToken, false);
        userService.save(user);

        AuthService.UserSignUpResponse reply = AuthService.UserSignUpResponse.newBuilder()
                .setStatus("success")
                .setUuid(user.getId().toString())
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
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

            String msg = "<div><h1>It's not scam, click to play!</h1><a href=\"http://localhost:8446/auth/activation?token="+token+"\"><h1>Start Game</h1></a>";

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