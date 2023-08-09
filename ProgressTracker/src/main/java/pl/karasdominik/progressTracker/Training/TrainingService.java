package pl.karasdominik.progressTracker.Training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.karasdominik.progressTracker.LoginController;

import java.util.List;

@RestController
public class TrainingService {

    @Autowired
    TrainingRepository trainingRepository;

    @Autowired
    LoginController loginController;

    @PostMapping("/add")
    public ResponseEntity<Training> addWorkoutSession(@RequestBody Training training){
        if(!loginController.isUserLoggedIn()) return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        training.setId_user(loginController.getLoggedUser().getUserID());
        Training savedTraining = trainingRepository.save(training);
        return ResponseEntity.ok(savedTraining);
    }

    @GetMapping("/getTrainings")
    public ResponseEntity<List<Training>> getAllTrainings(){
        if(!loginController.isUserLoggedIn()) return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        Long userLoggedId = loginController.getLoggedUser().getUserID();
        List<Training> listOfTrainings = trainingRepository.findAll().stream()
                .filter(training -> training.getId_user().equals(userLoggedId)).toList();
        return ResponseEntity.ok(listOfTrainings);
    }

    @DeleteMapping("/deleteWorkout")
    public boolean deleteTraining(Long id){
        if(!loginController.isUserLoggedIn()) return false;
        trainingRepository.deleteById(id);
        return true;
    }
}
