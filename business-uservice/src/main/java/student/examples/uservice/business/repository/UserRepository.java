package student.examples.uservice.business.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import student.examples.uservice.business.domain.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByToken(String token);

    User findUserByEmailAndPassword(String email, String password);

    @Transactional
    @Modifying
    @Query("update users set active = :active where token = :token")
    void updateActive(@Param("token") String token, @Param("active") boolean active);

    @Transactional
    @Modifying
    @Query("delete from users where token = :token")
    void removeUser(@Param("token") String token);
}
