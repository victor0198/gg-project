package student.examples.uservice.api.client.rest;

import com.example.grpc.AuthService;
import com.example.grpc.SignUpServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import student.examples.uservice.api.client.dto.WithdrawRequest;

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

        AuthService.UserManageRequest request =
                AuthService.UserManageRequest
                        .newBuilder()
                        .setUsername(userSignupRequest.getUsername())
                        .setEmail(userSignupRequest.getEmail())
                        .setPassword(userSignupRequest.getPassword())
                        .build();

        AuthService.UserManageResponse GRPCresponse = stub.signup(request);

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

        AuthService.AccountChangeRequest request =
                AuthService.AccountChangeRequest
                        .newBuilder()
                        .setToken(token)
                        .build();

        AuthService.AccountChangeResponse GRPCresponse = stub.activate(request);

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






    @PostMapping("/withdrawal")
    public student.examples.uservice.api.client.dto.RestResponse withdrawal(@Valid @RequestBody WithdrawRequest withdrawRequest){

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9090)
                .usePlaintext().build();

        SignUpServiceGrpc.SignUpServiceBlockingStub stub =
                SignUpServiceGrpc.newBlockingStub(channel);

        AuthService.UserManageRequest request =
                AuthService.UserManageRequest
                        .newBuilder()
                        .setEmail(withdrawRequest.getEmail())
                        .setPassword(withdrawRequest.getPassword())
                        .build();

        AuthService.UserManageResponse GRPCresponse = stub.withdraw(request);

        System.out.println(GRPCresponse);

        channel.shutdown();

        student.examples.uservice.api.client.dto.RestResponse response = new student.examples.uservice.api.client.dto.RestSuccessResponse(
                200,
                new HashMap<String, String>() {{
                    put(
                            "message",
                            String.format("An email has been sent to %s, please confirm account removal",
                                    withdrawRequest.getEmail()
                            )
                    );
                }}
        );
        return response;
    }


    @GetMapping("/withdrawal/confirmation")
    public student.examples.uservice.api.client.dto.RestResponse remove(@RequestParam(name = "token") String token) {
        token = token.replace(' ', '+');
        System.out.println("GOT token:" + token);

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9090)
                .usePlaintext().build();

        SignUpServiceGrpc.SignUpServiceBlockingStub stub =
                SignUpServiceGrpc.newBlockingStub(channel);

        AuthService.AccountChangeRequest request =
                AuthService.AccountChangeRequest
                        .newBuilder()
                        .setToken(token)
                        .build();

        AuthService.AccountChangeResponse GRPCresponse = stub.remove(request);

        System.out.println(GRPCresponse);

        channel.shutdown();

        student.examples.uservice.api.client.dto.RestResponse response;

        if(GRPCresponse.getMessage().equals("success")){
            response = new student.examples.uservice.api.client.dto.RestSuccessResponse(
                    200,
                    new HashMap<String, String>() {{
                        put(
                                "message",
                                "Account was not removed"
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
                            String.format("Account with token %s was removed",
                                    finalToken
                            )
                    );
                }}
        );
        return response;
    }
}
