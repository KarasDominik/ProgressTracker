package pl.karasdominik.progressTracker;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.karasdominik.progressTracker.User.InputValidator;
import pl.karasdominik.progressTracker.User.User;
import pl.karasdominik.progressTracker.User.UserAuthenticationService;
import pl.karasdominik.progressTracker.User.UserRepository;

import java.util.Optional;

@RestController
public class LoginController {

    @Autowired
    HttpSession httpSession;

    @Autowired
    InputValidator inputValidator;

    @Autowired
    UserAuthenticationService userAuthenticationService;

    @Autowired
    UserRepository userRepository;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<User> logUserIn(@RequestBody User user){

        if(!inputValidator.isUsernameAndPasswordValid(user)) return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();

        if(!userAuthenticationService.isUsernameAndPasswordCorrect(user)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        httpSession.setAttribute("loggedInUser", user.getUsername());

        return ResponseEntity.ok(user);
    }

    public boolean isUserLoggedIn() {
        String loggedInUser = (String) httpSession.getAttribute("loggedInUser");
        return loggedInUser != null;
    }

    @GetMapping("/getLoggedUser")
    public User getLoggedUser(){
        String loggedInUser = (String) httpSession.getAttribute("loggedInUser");
        Optional<User> user = userRepository.findByUsername(loggedInUser);

        return user.orElse(null);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logUserOut() {
        httpSession.invalidate();

        return ResponseEntity.ok().build();
    }
}
