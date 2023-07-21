package pl.karasdominik.progressTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity registerUser(@RequestBody User user) {

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
    public ResponseEntity logUserIn(@RequestBody User user){

        if(!inputValidator.isUsernameAndPasswordValid(user)) return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();

        if(userAuthenticationService.isUsernameAndPasswordCorrect(user)) return ResponseEntity.ok(user);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}


