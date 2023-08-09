package pl.karasdominik.progressTracker.Training;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.karasdominik.progressTracker.LoginController;

import java.util.List;

@RestController
public class TrainingService {

    @Autowired
    TrainingRepository trainingRepository;

    @Autowired
    LoginController loginController;

    @PostMapping("/add")
    public ResponseEntity<Training> addWorkoutSession(@RequestBody Training training, HttpSession session){
        if(!loginController.isUserLoggedIn(session)) return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        training.setId_user(loginController.getLoggedUser(session).getUserID());
        Training savedTraining = trainingRepository.save(training);
        return ResponseEntity.ok(savedTraining);
    }

    @GetMapping("/getTrainings")
    public ResponseEntity<List<Training>> getAllTrainings(HttpSession session){
        if(!loginController.isUserLoggedIn(session)) return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        Long userLoggedId = loginController.getLoggedUser(session).getUserID();
        List<Training> listOfTrainings = trainingRepository.findAll().stream()
                .filter(training -> training.getId_user().equals(userLoggedId)).toList();
        return ResponseEntity.ok(listOfTrainings);
    }
}
