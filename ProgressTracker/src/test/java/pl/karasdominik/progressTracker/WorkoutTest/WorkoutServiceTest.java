package pl.karasdominik.progressTracker.WorkoutTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.karasdominik.progressTracker.LoginController;
import pl.karasdominik.progressTracker.Workout.Workout;
import pl.karasdominik.progressTracker.Workout.WorkoutRepository;
import pl.karasdominik.progressTracker.Workout.WorkoutService;
import pl.karasdominik.progressTracker.User.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class WorkoutServiceTest {

    @Mock
    LoginController loginController;

    @Mock
    WorkoutRepository workoutRepository;

    @InjectMocks
    private WorkoutService workoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddWorkoutSessionWhenUserIsNotLoggedIn(){
        Workout workout = new Workout(5);

        when(loginController.isUserLoggedIn()).thenReturn(false);

        ResponseEntity<Workout> response = workoutService.addWorkoutSession(workout);

        assertEquals(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(), response);
    }

    @Test
    void testAddWorkoutSessionWhenUserIsLoggedIn(){
        Workout workout = new Workout(5);
        User loggedUser = new User(5L, "dominik", "dominik");

        when(loginController.isUserLoggedIn()).thenReturn(true);
        when(loginController.getLoggedUser()).thenReturn(loggedUser);

        ResponseEntity<Workout> response = workoutService.addWorkoutSession(workout);

        assertEquals(ResponseEntity.status(HttpStatus.OK).build(), response);
    }

    @Test
    void testGetAllTrainingsWhenUserHasNoTrainingsSaved(){
        User loggedUser = new User(5L, "dominik", "dominik");

        when(loginController.isUserLoggedIn()).thenReturn(true);
        when(loginController.getLoggedUser()).thenReturn(loggedUser);

        ResponseEntity<List<Workout>> response = workoutService.getAllWorkouts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new ArrayList<>(), response.getBody());
    }

    @Test
    void testGetAllTrainingsWhenUserHasSomeTrainingsSaved(){
        User loggedUser = new User(5L, "dominik", "dominik");

        when(loginController.isUserLoggedIn()).thenReturn(true);
        when(loginController.getLoggedUser()).thenReturn(loggedUser);

        List<Workout> loggedUserWorkouts = new ArrayList<>();
        Workout workout = new Workout(50);
        workout.setId_user(5L);
        loggedUserWorkouts.add(workout);
        when(workoutRepository.findAll()).thenReturn(loggedUserWorkouts);

        ResponseEntity<List<Workout>> response = workoutService.getAllWorkouts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loggedUserWorkouts, response.getBody());
    }

    @Test
    void testDeleteTrainingWhenUserIsLoggedIn(){
        Long trainingId = 5L;
        when(loginController.isUserLoggedIn()).thenReturn(true);

        boolean result = workoutService.deleteWorkout(trainingId);
        verify(workoutRepository, times(1)).deleteById(trainingId);

        assertTrue(result);
    }

}