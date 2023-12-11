package student.examples.uservice.api.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserSignupRequest {
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,}$", message = "should be at least 8 characters long")
    private String username;

    @Email(message = "should be valid")
    private String email;

    @Pattern(regexp = "[A-Za-z0-9_.@]{8,20}",
            message = "should consist of only letters and digits, and have a length between 8 and 20 characters")
    private String password;

    @NotEmpty(message = "confirmation shouldn't be empty")
    private String passwordConfirmation;

    @Override
    public String toString() {
        return "UserSignupRequest{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", passwordConfirmation='" + passwordConfirmation + '\'' +
                '}';
    }
}
