package pl.karasdominik.progressTracker.Workout;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "training")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTraining;

    @NonNull
    private Long id_user;

    private int distance;

    public Workout(int distance) {
        this.distance = distance;
    }
}
