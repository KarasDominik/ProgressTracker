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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkoutServiceTest {

    private Workout workout;
    private User loggedUser;

    @Mock
    LoginController loginController;

    @Mock
    WorkoutRepository workoutRepository;

    @InjectMocks
    private WorkoutService workoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        workout = new Workout(50);
        loggedUser = new User(5L, "loggedUser", "password");
    }

    @Test
    void testAddWorkoutSessionWhenUserIsNotLoggedIn(){
        when(loginController.isUserLoggedIn()).thenReturn(false);

        ResponseEntity<Workout> response = workoutService.addWorkoutSession(workout);

        assertEquals(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(), response);
    }

    @Test
    void testAddWorkoutSessionWhenUserIsLoggedIn(){
        when(loginController.isUserLoggedIn()).thenReturn(true);
        when(loginController.getLoggedUser()).thenReturn(loggedUser);

        ResponseEntity<Workout> response = workoutService.addWorkoutSession(workout);

        assertEquals(ResponseEntity.status(HttpStatus.OK).build(), response);
    }

    @Test
    void testGetAllTrainingsWhenUserHasNoTrainingsSaved(){
        when(loginController.isUserLoggedIn()).thenReturn(true);
        when(loginController.getLoggedUser()).thenReturn(loggedUser);

        ResponseEntity<List<Workout>> response = workoutService.getAllWorkouts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new ArrayList<>(), response.getBody());
    }

    @Test
    void testGetAllTrainingsWhenUserHasSomeTrainingsSaved(){
        when(loginController.isUserLoggedIn()).thenReturn(true);
        when(loginController.getLoggedUser()).thenReturn(loggedUser);

        List<Workout> loggedUserWorkouts = new ArrayList<>();
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

    @Test
    void testDeleteTrainingWhenUserIsNotLoggedIn(){
        Long trainingId = 5L;
        when(loginController.isUserLoggedIn()).thenReturn(false);

        boolean result = workoutService.deleteWorkout(trainingId);
        verify(workoutRepository, times(0)).deleteById(trainingId);

        assertFalse(result);
    }

    @Test
    void shouldNotUpdateAnyWorkoutSessionWhenUserIsNotLoggedIn(){
        when(loginController.isUserLoggedIn()).thenReturn(false);
        ResponseEntity<Workout> response = workoutService.updateWorkout(workout);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    
    @Test
    void shouldNotUpdateWorkoutSessionForUserDifferentThanLoggedIn(){
        workout.setId_user(5L);
        loggedUser.setUserID(6L);

        when(loginController.isUserLoggedIn()).thenReturn(true);
        when(workoutRepository.findById(workout.getIdTraining())).thenReturn(Optional.of(workout));
        when(loginController.getLoggedUser()).thenReturn(loggedUser);

        ResponseEntity<Workout> response = workoutService.updateWorkout(workout);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldUpdateWorkoutSessionForLoggedUser(){
        workout.setId_user(loggedUser.getUserID());

        when(loginController.isUserLoggedIn()).thenReturn(true);
        when(workoutRepository.findById(workout.getIdTraining())).thenReturn(Optional.of(workout));
        when(loginController.getLoggedUser()).thenReturn(loggedUser);

        ResponseEntity<Workout> response = workoutService.updateWorkout(workout);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldNotDeleteWorkoutIfNotFound(){
        workout.setId_user(4L);

        when(loginController.isUserLoggedIn()).thenReturn(true);
        when(workoutRepository.findById(workout.getIdTraining())).thenReturn(Optional.empty());
        when(loginController.getLoggedUser()).thenReturn(loggedUser);

        ResponseEntity<Workout> response = workoutService.updateWorkout(workout);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}