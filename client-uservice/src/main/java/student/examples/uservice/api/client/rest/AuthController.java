package student.examples.uservice.api.client.rest;

import com.example.grpc.AuthService;
import com.example.grpc.SignUpServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/signup")
    public student.examples.uservice.api.client.dto.RestResponse signup(@Valid @RequestBody student.examples.uservice.api.client.dto.UserSignupRequest userSignupRequest){

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9090)
                .usePlaintext().build();

        SignUpServiceGrpc.SignUpServiceBlockingStub stub =
                SignUpServiceGrpc.newBlockingStub(channel);

        AuthService.UserSignUpRequest request =
                AuthService.UserSignUpRequest
                        .newBuilder()
                        .setUsername(userSignupRequest.getUsername())
                        .setEmail(userSignupRequest.getEmail())
                        .setPassword(userSignupRequest.getPassword())
                        .build();

        AuthService.UserSignUpResponse GRPCresponse = stub.signup(request);

        System.out.println(GRPCresponse);

        channel.shutdown();

        student.examples.uservice.api.client.dto.RestResponse response = new student.examples.uservice.api.client.dto.RestSuccessResponse(
            200,
            new HashMap<String, String>() {{
                put(
                    "message",
                    String.format("An email has been sent to %s, please activate the account",
                            userSignupRequest.getEmail()
                    )
                );
            }}
        );
        return response;
    }

    @PostMapping("/signin")
    public void signin(@Valid @RequestBody student.examples.uservice.api.client.dto.UserSigninRequest userSigninRequest){

    }

    @GetMapping("/activation")
    public student.examples.uservice.api.client.dto.RestResponse signup(@RequestParam(name = "token") String token) {
        token = token.replace(' ', '+');
        System.out.println("GOT token:" + token);

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9090)
                .usePlaintext().build();

        SignUpServiceGrpc.SignUpServiceBlockingStub stub =
                SignUpServiceGrpc.newBlockingStub(channel);

        AuthService.ActivationRequest request =
                AuthService.ActivationRequest
                        .newBuilder()
                        .setToken(token)
                        .build();

        AuthService.ActivationResponse GRPCresponse = stub.activate(request);

        System.out.println(GRPCresponse);

        channel.shutdown();

        student.examples.uservice.api.client.dto.RestResponse response;

        if(GRPCresponse.getMessage().equals("success")){
            response = new student.examples.uservice.api.client.dto.RestSuccessResponse(
                    200,
                    new HashMap<String, String>() {{
                        put(
                                "message",
                                "Account was not activated"
                        );
                    }}
            );
            return response;
        }

        String finalToken = token;
        response = new student.examples.uservice.api.client.dto.RestSuccessResponse(
                200,
                new HashMap<String, String>() {{
                    put(
                            "messsage",
                            String.format("Account with token %s was activated",
                                    finalToken
                            )
                    );
                }}
        );
        return response;
    }
}
