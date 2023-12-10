package student.examples.uservice.api.client;

import com.example.grpc.SignUpServiceGrpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SignUpServiceGrpc.class)
public class ClientUserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientUserviceApplication.class, args);
	}

}
