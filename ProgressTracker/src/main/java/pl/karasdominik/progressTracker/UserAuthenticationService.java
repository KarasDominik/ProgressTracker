package pl.karasdominik.progressTracker;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserAuthenticationService {

    @Autowired
    UserRepository userRepository;

    public boolean isUsernameUnique(User user) {
        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());
        return userFromDb.isEmpty();
    }

    public boolean isUsernameAndPasswordCorrect(User user){
        return userRepository.findAll().contains(user);
    }
}
