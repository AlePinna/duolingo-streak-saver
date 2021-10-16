package eu.alessandropinna.streaksaver;

import eu.alessandropinna.utils.PasswordUtils;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.security.NoSuchAlgorithmException;

@SpringBootApplication(scanBasePackages = {"eu.alessandropinna"})
@EnableBatchProcessing
@EnableScheduling
public class DuolingoStreakSaverApplication {

    public static void main(String[] args) {
        SpringApplication.run(DuolingoStreakSaverApplication.class, args);
    }

    @Bean
    public PasswordUtils passwordUtils(@Value("${encryption.key}") String key64) throws NoSuchAlgorithmException {
        return new PasswordUtils(key64);
    }
}
