package pl.karasdominik.progressTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        if(!inputValidator.isUsernameAndPasswordValid(user)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(!userAuthenticationService.isUsernameUnique(user)){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<User> logUserIn(@RequestBody User user){

        if(!inputValidator.isUsernameAndPasswordValid(user)) return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();

        if(userAuthenticationService.isUsernameAndPasswordCorrect(user)) return ResponseEntity.ok(user);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/users")
    public List<User> getAll(){
        return userRepository.findAll();
    }

    @DeleteMapping("/delete")
    public boolean deleteUserById(Long id){
        if(userRepository.findById(id).isEmpty()) return false;
        userRepository.deleteById(id);
        return true;
    }

    @PatchMapping("/update")
    public ResponseEntity<User> changeUserPassword(@RequestBody User user){
        if(!inputValidator.isUsernameAndPasswordValid(user)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());

        if(userFromDb.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        User existingUser = userFromDb.get();
        existingUser.setPassword(user.getPassword());

        User savedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(savedUser);
    }
}


