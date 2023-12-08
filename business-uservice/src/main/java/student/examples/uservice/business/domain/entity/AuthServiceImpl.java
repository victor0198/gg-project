package student.examples.uservice.business.domain.entity;
//
//import com.example.grpc.AuthServiceGrpc;
//import com.example.grpc.AuthServiceOuterClass.SignInRequest;
//import com.example.grpc.AuthServiceOuterClass.SignInResponse;
import com.example.grpc.AuthService;
//import com.example.grpc.GreetingServiceGrpc;
import com.example.grpc.SignIpServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import student.examples.uservice.business.services.UserService;

//import java.util.UUID;
import net.devh.boot.grpc.server.service.GrpcService;
import student.examples.uservice.business.repository.UserRepository;

import java.util.UUID;

@GrpcService
public class AuthServiceImpl extends SignIpServiceGrpc.SignIpServiceImplBase {

    @Autowired
    UserRepository userRepository;

    @Override
    public void signing(AuthService.UserSignUpRequest request, StreamObserver<AuthService.UserSignUpResponse> responseObserver) {
        User newUser = userRepository.save(new User(UUID.randomUUID(), request.getEmail(), request.getUsername(), request.getPassword()));

        AuthService.UserSignUpResponse reply = AuthService.UserSignUpResponse.newBuilder()
                .setStatus("success")
                .setUuid(newUser.getId().toString())
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}