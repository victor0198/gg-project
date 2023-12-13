package student.examples.uservice.api.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class WithdrawRequest {

    @Email(message = "should be valid")
    private String email;

    @Pattern(regexp = "[A-Za-z0-9_.@]{8,20}",
            message = "should consist of only letters and digits, and have a length between 8 and 20 characters")
    private String password;

}
