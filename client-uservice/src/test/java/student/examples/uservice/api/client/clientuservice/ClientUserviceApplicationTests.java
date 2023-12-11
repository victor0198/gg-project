package student.examples.uservice.api.client.clientuservice;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import student.examples.uservice.api.client.dto.UserSignupRequest;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientUserviceApplicationTests {
	private static ValidatorFactory validatorFactory;
	private static Validator validator;
	@Test
	void contextLoads() throws IllegalAccessException {

		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();

		UserSignupRequest userSignupRequest = new UserSignupRequest();

		String validationResults="";
		String file = GitHubFileReader.getCSV();
		try (StringReader stringReader = new StringReader(file);
			 CSVReader csvReader = new CSVReader(stringReader)) {

			String[] line;
			while ((line = csvReader.readNext()) != null) {
				Field usermaneField = UserSignupRequest.class.getDeclaredField("username");
				Field emailField = UserSignupRequest.class.getDeclaredField("email");
				Field passwordField = UserSignupRequest.class.getDeclaredField("password");
				Field passwordConfirmationField = UserSignupRequest.class.getDeclaredField("passwordConfirmation");

				usermaneField.setAccessible(true);
				emailField.setAccessible(true);
				passwordField.setAccessible(true);
				passwordConfirmationField.setAccessible(true);

				usermaneField.set(userSignupRequest, line[0]);
				emailField.set(userSignupRequest, line[1]);
				passwordField.set(userSignupRequest, line[2]);
				passwordConfirmationField.set(userSignupRequest, line[3]);

				System.out.println("Row: " + String.join("|", line));

				Set<ConstraintViolation<UserSignupRequest>> violations = validator.validate(userSignupRequest);
				validationResults = validationResults.concat("\n").concat(violations.toString());
				System.out.println(violations);

				System.out.println(violations.isEmpty());
			}
		} catch (IOException | CsvValidationException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

		GitHubFileUploader.upload(validationResults);

		validatorFactory.close();
	}

	public class GitHubFileReader {

		public static String getCSV() {
			String owner = "victor0198";
			String repo = "test-data";
			String filePath = "UserSignUpValidation.txt";

			String fileContent="";
			try {
				fileContent = fetchGitHubFileContent(owner, repo, filePath);
				System.out.println("File Content:\n" + fileContent);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return fileContent;
		}

		private static String fetchGitHubFileContent(String owner, String repo, String filePath) throws Exception {
			String apiUrl = "https://raw.githubusercontent.com/" + owner + "/" + repo + "/main/" + filePath;

			HttpClient httpClient = HttpClient.newHttpClient();
			HttpRequest httpRequest = HttpRequest.newBuilder()
					.uri(URI.create(apiUrl))
					.GET()
					.build();

			HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200) {
				return response.body();
			} else {
				throw new RuntimeException("Failed to fetch file content. HTTP Status Code: " + response.statusCode());
			}
		}
	}

	public class GitHubFileUploader {

		public static void upload(String content) {
			String owner = "victor0198";
			String repo = "test-data";
			String filePath = "UserSignUpValidationResults.txt";
			String githubToken = "****";

			try {
				uploadFileToGitHub(owner, repo, filePath, githubToken, content);
				System.out.println("File uploaded successfully.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private static void uploadFileToGitHub(String owner, String repo, String filePath, String githubToken, String content) throws Exception {
			String apiUrl = "https://api.github.com/repos/" + owner + "/" + repo + "/contents/" + filePath;

			byte[] fileContent = content.getBytes();
			String base64Content = java.util.Base64.getEncoder().encodeToString(fileContent);

			String commitMessage = "Upload file via API";

			HttpClient httpClient = HttpClient.newHttpClient();

			HttpRequest httpRequest = HttpRequest.newBuilder()
					.uri(URI.create(apiUrl))
					.header("Authorization", "Bearer " + githubToken)
					.header("Content-Type", "application/json")
					.PUT(HttpRequest.BodyPublishers.ofString(createRequestBody(base64Content, commitMessage)))
					.build();

			HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 201) {
				throw new RuntimeException("Failed to upload file. HTTP Status Code: " + response.statusCode());
			}
		}

		private static String createRequestBody(String base64Content, String commitMessage) {
			return "{\"message\":\"" + commitMessage + "\",\"content\":\"" + base64Content + "\"}";
		}
	}

}


