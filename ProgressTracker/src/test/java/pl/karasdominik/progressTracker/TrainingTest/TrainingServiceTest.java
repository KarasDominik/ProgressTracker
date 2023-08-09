package pl.karasdominik.progressTracker.TrainingTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.karasdominik.progressTracker.LoginController;
import pl.karasdominik.progressTracker.Training.Training;
import pl.karasdominik.progressTracker.Training.TrainingRepository;
import pl.karasdominik.progressTracker.Training.TrainingService;
import pl.karasdominik.progressTracker.User.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class TrainingServiceTest {

    @Mock
    LoginController loginController;

    @Mock
    TrainingRepository trainingRepository;

    @InjectMocks
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddWorkoutSessionWhenUserIsNotLoggedIn(){
        Training training = new Training(5);

        when(loginController.isUserLoggedIn()).thenReturn(false);

        ResponseEntity<Training> response = trainingService.addWorkoutSession(training);

        assertEquals(ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build(), response);
    }

    @Test
    void testAddWorkoutSessionWhenUserIsLoggedIn(){
        Training training = new Training(5);
        User loggedUser = new User(5L, "dominik", "dominik");

        when(loginController.isUserLoggedIn()).thenReturn(true);
        when(loginController.getLoggedUser()).thenReturn(loggedUser);

        ResponseEntity<Training> response = trainingService.addWorkoutSession(training);

        assertEquals(ResponseEntity.status(HttpStatus.OK).build(), response);
    }

}