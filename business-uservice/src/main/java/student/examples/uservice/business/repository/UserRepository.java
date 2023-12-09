package student.examples.uservice.business.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import student.examples.uservice.business.domain.entity.User;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByToken(String token);
    @Transactional
    @Modifying
    @Query("update users set active = :active where token = :token")
    void updateActive(@Param(value = "token") String token, @Param(value = "active") boolean active);
}
