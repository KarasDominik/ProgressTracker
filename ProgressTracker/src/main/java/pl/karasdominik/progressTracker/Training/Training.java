package pl.karasdominik.progressTracker.Training;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "training")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTraining;

    @NonNull
    private Long id_user;

    private int distance;

    public Training(int distance) {
        this.distance = distance;
    }
}
