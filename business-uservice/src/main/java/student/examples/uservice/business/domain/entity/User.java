package student.examples.uservice.business.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User {

    @Id
    private UUID id;

    @Column(length=50, nullable=false, unique=true)
    private String email;

    @Column(length=50, nullable=false, unique=true)
    private String username;

    @Column(nullable=false)
    private String password;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
