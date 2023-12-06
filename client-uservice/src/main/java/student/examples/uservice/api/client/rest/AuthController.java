package student.examples.uservice.api.client.rest;

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
    public RestResponse signup(@Valid @RequestBody UserSignupRequest userSignupRequest
//            , BindingResult bindingResult
    ){

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
