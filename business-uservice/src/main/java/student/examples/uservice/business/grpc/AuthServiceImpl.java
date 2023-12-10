package student.examples.uservice.business.grpc;

import com.example.grpc.AuthService;
import com.example.grpc.SignUpServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import student.examples.uservice.business.domain.entity.User;
import student.examples.uservice.business.service.UserService;

import java.util.UUID;

import student.examples.uservice.business.util.TokenManager;

@GrpcService
public class AuthServiceImpl extends SignUpServiceGrpc.SignUpServiceImplBase {

    @Autowired
    UserService userService;

    @Override
    public void signup(AuthService.UserManageRequest request, StreamObserver<AuthService.UserManageResponse> responseObserver) {
        UUID uuid = UUID.randomUUID();

        String ggToken = TokenManager.buildGGToken(uuid, request.getEmail());


        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8447/email/activate";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonPayload = "{ \"email\": \""+request.getEmail()+"\"," +
                " \"token\": \""+ggToken+"\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
        String responseBody = responseEntity.getBody();
        System.out.println("Response: " + responseBody);

        if(!responseBody.equals("OK")){
            AuthService.UserManageResponse reply = AuthService.UserManageResponse.newBuilder()
                    .setStatus("failed")
                    .build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }

        User user = new User(uuid, request.getEmail(), request.getUsername(), request.getPassword(), ggToken, false);
        userService.save(user);

        AuthService.UserManageResponse reply = AuthService.UserManageResponse.newBuilder()
                .setStatus("success")
                .setUuid(user.getId().toString())
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void activate(AuthService.AccountChangeRequest request, StreamObserver<AuthService.AccountChangeResponse> responseObserver) {

        String token = request.getToken();
        System.out.println("GOT token:" + token);

        boolean activated = userService.activate(token);

        AuthService.AccountChangeResponse reply;
        if(!activated){
            reply = AuthService.AccountChangeResponse.newBuilder()
                    .setStatus("failed")
                    .setMessage("Email was not activated")
                    .build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }else {

            reply = AuthService.AccountChangeResponse.newBuilder()
                    .setStatus("success")
                    .setMessage("Email was activated")
                    .build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }

    }


    @Override
    public void withdraw(AuthService.UserManageRequest request, StreamObserver<AuthService.UserManageResponse> responseObserver) {
        User user = userService.findByEmailAndPassword(request.getEmail(), request.getPassword());

        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8447/email/withdraw";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonPayload = "{ \"email\": \""+request.getEmail()+"\"," +
                " \"token\": \""+user.getToken()+"\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
        String responseBody = responseEntity.getBody();
        System.out.println("Response: " + responseBody);

        if(!responseBody.equals("OK")){
            AuthService.UserManageResponse reply = AuthService.UserManageResponse.newBuilder()
                    .setStatus("failed")
                    .build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }else {
            AuthService.UserManageResponse reply = AuthService.UserManageResponse.newBuilder()
                    .setStatus("success")
                    .setUuid(user.getId().toString())
                    .build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void remove(AuthService.AccountChangeRequest request, StreamObserver<AuthService.AccountChangeResponse> responseObserver) {

        String token = request.getToken();
        System.out.println("GOT token:" + token);

        userService.remove(token);

        AuthService.AccountChangeResponse reply = AuthService.AccountChangeResponse.newBuilder()
                    .setStatus("success")
                    .setMessage("Account was removed")
                    .build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();

    }



}