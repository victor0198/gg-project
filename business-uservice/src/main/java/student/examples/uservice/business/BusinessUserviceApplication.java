package student.examples.uservice.business;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import student.examples.uservice.business.domain.entity.AuthServiceImpl;
import student.examples.uservice.business.repository.UserRepository;
import student.examples.uservice.business.services.UserService;

import java.io.IOException;

@SpringBootApplication
public class BusinessUserviceApplication {

	public static void main(String[] args) throws InterruptedException, IOException {

		SpringApplication.run(BusinessUserviceApplication.class, args);

//		Server server = ServerBuilder
//				.forPort(9090)
//				.addService(hServiceImpl)
//				.build();
//		server.start();
//		System.out.println("Server started");
//
//		server.awaitTermination();
	}

}

