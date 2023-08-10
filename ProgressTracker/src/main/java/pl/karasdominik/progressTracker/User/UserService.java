package pl.karasdominik.progressTracker.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    InputValidator inputValidator;

    @Autowired
    UserAuthenticationService userAuthenticationService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {

        if(!inputValidator.isUsernameAndPasswordValid(user)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        if(!userAuthenticationService.isUsernameUnique(user)) return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();

        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/getUsers")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @DeleteMapping("/deleteUser")
    public boolean deleteUserById(Long id){
        if(userRepository.findById(id).isEmpty()) return false;
        userRepository.deleteById(id);
        return true;
    }

    @PatchMapping("/updateUser")
    public ResponseEntity<User> changeUserPassword(@RequestBody User user){
        if(!inputValidator.isUsernameAndPasswordValid(user)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());

        if(userFromDb.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        User existingUser = userFromDb.get();
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        existingUser.setPassword(hashedPassword);

        User savedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(savedUser);
    }
}


