package pl.karasdominik.progressTracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InputValidator inputValidator;

    @Mock
    private UserAuthenticationService userAuthenticationService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUserWhenUserAlreadyExists(){
        User existingUser = new User("existingUser", "password");

        when(inputValidator.isUsernameAndPasswordValid(existingUser)).thenReturn(true);
        when(userRepository.findByUsername(existingUser.getUsername())).thenReturn(Optional.of(existingUser));

        ResponseEntity<User> responseEntity = userService.registerUser(existingUser);

        assertEquals(ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build(), responseEntity);
    }

    @Test
    void testRegisterUserWhenUserDoesNotExist(){
        User newUser = new User("newUser", "password");

        when(inputValidator.isUsernameAndPasswordValid(newUser)).thenReturn(true);
        when(userAuthenticationService.isUsernameUnique(newUser)).thenReturn(true);
        when(userRepository.save(newUser)).thenReturn(newUser);

        ResponseEntity<User> response = userService.registerUser(newUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newUser, response.getBody());
    }

    @Test
    void testDeleteUserById(){
        Long idOfUserToDelete = 1L;

        User existingUser = new User(idOfUserToDelete, "username", "password");

        when(userRepository.findById(idOfUserToDelete)).thenReturn(Optional.of(existingUser));

        assertTrue(userService.deleteUserById(idOfUserToDelete));
    }

    @Test
    void testDeleteUserByIdWhenUserWithThisIdDoesNotExist(){
        Long idOfUserToDelete = 1L;

        when(userRepository.findById(idOfUserToDelete)).thenReturn(Optional.empty());

        assertFalse(userService.deleteUserById(idOfUserToDelete));
    }
}