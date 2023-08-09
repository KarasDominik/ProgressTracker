package pl.karasdominik.progressTracker.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAuthenticationService {

    @Autowired
    UserRepository userRepository;

    public boolean isUsernameUnique(User user) {
        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());
        return userFromDb.isEmpty();
    }

    public boolean isUsernameAndPasswordCorrect(User user){
        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());
        return userFromDb.isPresent() && BCrypt.checkpw(user.getPassword(), userFromDb.get().getPassword());
    }
}
