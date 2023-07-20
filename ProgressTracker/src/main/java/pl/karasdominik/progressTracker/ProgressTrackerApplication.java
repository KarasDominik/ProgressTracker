package pl.karasdominik.progressTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "pl.karasdominik.progressTracker")
public class ProgressTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProgressTrackerApplication.class, args);
	}

}
