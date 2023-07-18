package pl.karasdominik.progressTracker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/users")
    public ResponseEntity addUser(@RequestBody User user) {

        InputValidator inputValidator = new InputValidator();

        if(!isUsernameUnique(user)){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        if(!inputValidator.isUsernameAndPasswordValid(user)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    public boolean isUsernameUnique(User user) {
        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());
        return userFromDb.isEmpty();
    }

    @GetMapping("/users")
    public ResponseEntity getUsers() throws JsonProcessingException {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(objectMapper.writeValueAsString(users));
    }
}


