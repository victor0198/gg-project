package student.examples.uservice.api.client.clientuservice;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import student.examples.uservice.api.client.dto.UserSignupRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestFieldValidation {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Test
    void fieldsValid() throws Exception {

        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();

        String repositoryURL = "https://github.com/victor0198/test-data.git";
        String localPath = "C:\\Users\\User\\Videos\\test-data";

        UserSignupRequest userSignupRequest = new UserSignupRequest();
        String validationResults="";


        String fileContent = "";
        File folder = new File(localPath);
        if (folder.exists() && folder.isDirectory()) {
            System.out.println("The folder exists.");
            FileUtils.deleteDirectory(folder);
        }
        Git git = Git.cloneRepository()
                .setURI(repositoryURL)
                .setDirectory(new File(localPath))
                .call();

        try (Repository repository = git.getRepository()) {
            String filePath = "UserSignUpValidation.txt";
            fileContent = readContent(repository, filePath);
            System.out.println("File Content:\n" + fileContent);
        }

        assertFalse(fileContent.isEmpty());


        try (StringReader stringReader = new StringReader(fileContent);
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


        createNewFile(git, "UserSignUpValidationResults.txt", validationResults);
        commitChanges(git, "trial 2");
        addTag(git, "dao-validation");
        pushToMainBranch(git);
    }

    private static String readContent(Repository repository, String filePath) throws Exception {
        try (ObjectReader reader = repository.newObjectReader()) {
            ObjectId objectId = repository.resolve("HEAD:" + filePath);

            ObjectLoader loader = reader.open(objectId);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            loader.copyTo(out);
            return out.toString();
        }
    }

    private static void createNewFile(Git git, String filePath, String content) throws IOException, GitAPIException {
        File file = new File(git.getRepository().getWorkTree(), filePath);
        FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);
        git.add().addFilepattern(filePath).call();
    }

    private static void commitChanges(Git git, String commitMessage) throws GitAPIException {
        git.commit().setMessage(commitMessage).call();
    }

    private static void addTag(Git git, String tagName) throws GitAPIException {
        git.tag().setName(tagName).call();
    }

    private static void pushToMainBranch(Git git) throws GitAPIException {
        git.pull().call();

        PushCommand pushCommand = git.push();

        RefSpec refSpec = new RefSpec("refs/heads/main:refs/heads/main");
        pushCommand.setRemote("origin").setRefSpecs(refSpec);

        UsernamePasswordCredentialsProvider credentialsProvider =
                new UsernamePasswordCredentialsProvider("victor0198", "***");
        pushCommand.setCredentialsProvider(credentialsProvider);

        pushCommand.call();

        System.out.println("Pushed changes to the main branch.");
    }


}


