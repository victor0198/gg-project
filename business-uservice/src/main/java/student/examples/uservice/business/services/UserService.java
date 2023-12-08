package student.examples.uservice.business.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import student.examples.uservice.business.domain.entity.User;
import student.examples.uservice.business.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user){
        System.out.println(user);
        userRepository.save(user);
        return user;
    }
}
