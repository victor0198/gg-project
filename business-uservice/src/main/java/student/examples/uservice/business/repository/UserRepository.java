package student.examples.uservice.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import student.examples.uservice.business.domain.entity.User;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
