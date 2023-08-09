package pl.karasdominik.progressTracker;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.karasdominik.progressTracker.User.InputValidator;
import pl.karasdominik.progressTracker.User.User;
import pl.karasdominik.progressTracker.User.UserAuthenticationService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    @Mock
    private InputValidator inputValidator;

    @Mock
    private UserAuthenticationService userAuthenticationService;

    @Mock
    private HttpSession httpSession;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogUserInWhenCredentialsAreCorrect(){
        User existingUser = new User("validUsername", "validPassword");

        when(inputValidator.isUsernameAndPasswordValid(existingUser)).thenReturn(true);
        when(userAuthenticationService.isUsernameAndPasswordCorrect(existingUser)).thenReturn(true);

        ResponseEntity<User> response = loginController.logUserIn(existingUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingUser, response.getBody());
    }

    @Test
    void testLogUserInWhenCredentialsAreNotCorrect(){
        User nonExistingUser = new User("invalidUsername", "invalidPassword");

        when(inputValidator.isUsernameAndPasswordValid(nonExistingUser)).thenReturn(true);
        when(userAuthenticationService.isUsernameAndPasswordCorrect(nonExistingUser)).thenReturn(false);

        ResponseEntity<User> response = loginController.logUserIn(nonExistingUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}