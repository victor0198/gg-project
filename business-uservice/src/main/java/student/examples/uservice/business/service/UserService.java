package student.examples.uservice.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import student.examples.uservice.business.domain.entity.User;
import student.examples.uservice.business.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user){
        System.out.println("Saving:"+user);
        userRepository.save(user);
        return user;
    }

    public boolean activate(String token){
        System.out.println("Activating:"+token);
        userRepository.updateActive(token, true);
        User user = userRepository.findByToken(token);
        return user != null && user.isActive();
    }

    public void remove(String token){
        System.out.println("Removing:"+token);
        userRepository.removeUser(token);
    }

    public User findByEmailAndPassword(String email, String password){
        return userRepository.findUserByEmailAndPassword(email, password);
    }


}
