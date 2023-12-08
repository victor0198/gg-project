package student.examples.uservice.api.client.rest;

//import com.example.grpc.AuthServiceGrpc;
//import com.example.grpc.AuthServiceOuterClass;
//import com.example.grpc.AuthService;
//import com.example.grpc.GreetingServiceGrpc;
//import com.example.grpc.SignIpServiceGrpc;
import com.example.grpc.AuthService;
import com.example.grpc.SignIpServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import student.examples.uservice.api.client.dto.RestResponse;
import student.examples.uservice.api.client.dto.RestSuccessResponse;
import student.examples.uservice.api.client.dto.UserSigninRequest;
import student.examples.uservice.api.client.dto.UserSignupRequest;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/signup")
    public RestResponse signup(@Valid @RequestBody UserSignupRequest userSignupRequest){

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9090)
                .usePlaintext().build();

        SignIpServiceGrpc.SignIpServiceBlockingStub stub =
                SignIpServiceGrpc.newBlockingStub(channel);

        AuthService.UserSignUpRequest request =
                AuthService.UserSignUpRequest
                        .newBuilder()
                        .setUsername(userSignupRequest.getUsername())
                        .setEmail(userSignupRequest.getEmail())
                        .setPassword(userSignupRequest.getPassword())
                        .build();

        AuthService.UserSignUpResponse GRPCresponse = stub.signing(request);

        System.out.println(GRPCresponse);

        channel.shutdown();

        RestResponse response = new RestSuccessResponse(
            200,
            new HashMap<String, String>() {{
                put(
                    "messsage",
                    String.format("An email has been sent to %s, please activate the account",
                            userSignupRequest.getEmail()
                    )
                );
            }}
        );
        return response;
    }

    @PostMapping("/signin")
    public void signin(@Valid @RequestBody UserSigninRequest userSigninRequest){

    }
}
