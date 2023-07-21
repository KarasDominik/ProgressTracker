package pl.karasdominik.progressTracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UserAuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAuthenticationService userAuthenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsUsernameUniqueWhenUsernameIsUniqueShouldReturnTrue() {
        User user = new User("unique_username", "password");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        boolean isUnique = userAuthenticationService.isUsernameUnique(user);

        assertTrue(isUnique);
        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @Test
    void testIsUsernameUniqueWhenUsernameIsNotUniqueShouldReturnFalse(){
        User existingUser = new User("existing_username", "password");
        when(userRepository.findByUsername(existingUser.getUsername())).thenReturn(Optional.of(existingUser));

        boolean isUnique = userAuthenticationService.isUsernameUnique(existingUser);

        assertFalse(isUnique);
        verify(userRepository, times(1)).findByUsername(existingUser.getUsername());
    }

    @Test
    void testIsUsernameAndPasswordCorrect_whenUserExists_shouldReturnTrue() {
        User existingUser = new User("existing_username", "password");
        List<User> userList = new ArrayList<>();
        userList.add(existingUser);
        when(userRepository.findAll()).thenReturn(userList);

        boolean userExists = userAuthenticationService.isUsernameAndPasswordCorrect(existingUser);

        assertTrue(userExists);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testIsUsernameAndPasswordCorrect_whenUserDoesNotExist_shouldReturnFalse() {
        User newUser = new User("new_username", "password");
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);

        boolean userExists = userAuthenticationService.isUsernameAndPasswordCorrect(newUser);

        assertFalse(userExists);
        verify(userRepository, times(1)).findAll();
    }
}