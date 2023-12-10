package student.examples.uservice.business.grpc;

import com.example.grpc.AuthService;
import com.example.grpc.SignUpServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;



//import java.util.UUID;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import student.examples.uservice.business.domain.entity.User;
import student.examples.uservice.business.service.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;

import student.examples.uservice.business.util.TokenManager;

@GrpcService
public class AuthServiceImpl extends SignUpServiceGrpc.SignUpServiceImplBase {

    @Autowired
    UserService userService;

    @Override
    public void signup(AuthService.UserSignUpRequest request, StreamObserver<AuthService.UserSignUpResponse> responseObserver) {
        UUID uuid = UUID.randomUUID();

        String ggToken = TokenManager.buildGGToken(uuid, request.getEmail());


        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8447/email/send";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonPayload = "{ \"email\": \""+request.getEmail()+"\"," +
                " \"token\": \""+ggToken+"\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
        String responseBody = responseEntity.getBody();
        System.out.println("Response: " + responseBody);

        if(!responseBody.equals("OK")){
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

    @Override
    public void activate(AuthService.ActivationRequest request, StreamObserver<AuthService.ActivationResponse> responseObserver) {

        String token = request.getToken();
        System.out.println("GOT token:" + token);

        boolean activated = userService.activate(token);

        AuthService.ActivationResponse reply;
        if(!activated){
            reply = AuthService.ActivationResponse.newBuilder()
                    .setStatus("failed")
                    .setMessage("Email was not activated")
                    .build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }else {

            reply = AuthService.ActivationResponse.newBuilder()
                    .setStatus("success")
                    .setMessage("Email was activated")
                    .build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }

    }


}