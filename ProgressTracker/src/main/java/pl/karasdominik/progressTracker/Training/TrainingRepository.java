package pl.karasdominik.progressTracker.Training;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.karasdominik.progressTracker.Training.Training;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
}
