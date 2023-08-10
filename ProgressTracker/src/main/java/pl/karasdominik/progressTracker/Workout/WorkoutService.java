package pl.karasdominik.progressTracker.Workout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.karasdominik.progressTracker.LoginController;

import java.util.List;

@RestController
public class WorkoutService {

    @Autowired
    WorkoutRepository workoutRepository;

    @Autowired
    LoginController loginController;

    @PostMapping("/addWorkout")
    public ResponseEntity<Workout> addWorkoutSession(@RequestBody Workout workout){
        if(!loginController.isUserLoggedIn()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        workout.setId_user(loginController.getLoggedUser().getUserID());
        Workout savedWorkout = workoutRepository.save(workout);
        return ResponseEntity.ok(savedWorkout);
    }

    @GetMapping("/getWorkouts")
    public ResponseEntity<List<Workout>> getAllWorkouts(){
        if(!loginController.isUserLoggedIn()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Long userLoggedId = loginController.getLoggedUser().getUserID();
        List<Workout> listOfWorkouts = workoutRepository.findAll().stream()
                .filter(workout -> workout.getId_user().equals(userLoggedId)).toList();
        return ResponseEntity.ok(listOfWorkouts);
    }

    @DeleteMapping("/deleteWorkout")
    public boolean deleteWorkout(Long id){
        if(!loginController.isUserLoggedIn()) return false;
        workoutRepository.deleteById(id);
        return true;
    }

//    @PatchMapping("/updateWorkout")
//    public ResponseEntity<Workout> updateWorkout(@RequestBody Workout workout){
//
//        if(!loginController.isUserLoggedIn()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//
//        Optional<Workout> optionalWorkoutToUpdate = workoutRepository.findById(workout.getIdTraining());
//        if(optionalWorkoutToUpdate.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//
//        Workout workoutToUpdate = optionalWorkoutToUpdate.get();
//        if(!workoutToUpdate.getId_user().equals(loginController.getLoggedUser().getUserID())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//
//        workoutToUpdate.setDistance(workout.getDistance());
//
//        Workout savedWorkout = workoutRepository.save(workoutToUpdate);
//        return ResponseEntity.ok(savedWorkout);
//    }
}
