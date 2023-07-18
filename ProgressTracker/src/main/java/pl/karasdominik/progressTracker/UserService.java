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
public class UserService{

    int MIN_NUMBER_OF_CHARACTERS_FOR_USERNAME = 6;
    int MAX_NUMBER_OF_CHARACTERS_FOR_USERNAME = 15;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/users")
    public ResponseEntity getUsers() throws JsonProcessingException {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(objectMapper.writeValueAsString(users));
    }

    @PostMapping("/users")
    public ResponseEntity addUser(@RequestBody User user){

        if(!isUsernameUnique(user)){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        if(!isUsernameValid(user)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    private boolean isUsernameUnique(User user){
        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());
        return userFromDb.isEmpty();
    }

    private boolean isUsernameValid(User user){
        String username = user.getUsername();
        return hasAppropriateNumberOfCharacters(username) && containsOnlyAllowedCharacters(username);
    }

    private boolean hasAppropriateNumberOfCharacters(String username){
        return username.length() >= MIN_NUMBER_OF_CHARACTERS_FOR_USERNAME &&
                username.length() <= MAX_NUMBER_OF_CHARACTERS_FOR_USERNAME;
    }

    private boolean containsOnlyAllowedCharacters(String username){
        return InputValidator.isLoginValid(username);
    }
}
