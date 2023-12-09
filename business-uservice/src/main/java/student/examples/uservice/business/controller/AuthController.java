package student.examples.uservice.business.controller;

import com.example.grpc.AuthService;
import com.example.grpc.SignIpServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import student.examples.uservice.business.dto.RestErrorResponse;
import student.examples.uservice.business.dto.RestResponse;
import student.examples.uservice.business.dto.RestSuccessResponse;
import student.examples.uservice.business.service.UserService;
import student.examples.uservice.business.util.TokenManager;

import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @GetMapping("/activation")
    public RestResponse signup(@RequestParam(name = "token") String token) {
        token = token.replace(' ', '+');
        System.out.println("GOT token:" + token);


        boolean activated = userService.activate(token);

        RestResponse response;
        if(!activated){
            response = new RestErrorResponse(
                    400,
                    new HashMap<String, String>() {{
                        put("messsage",
                            String.format("Email %s was not activated",
                                    "email"//userSignupRequest.getEmail()
                            )
                        );
                    }}
            );
            return response;
        }

        response = new RestSuccessResponse(
                200,
                new HashMap<String, String>() {{
                    put("messsage",
                        String.format("Email %s has been activated",
                                "email"//userSignupRequest.getEmail()
                        )
                    );
                }}
        );
        return response;
    }
}