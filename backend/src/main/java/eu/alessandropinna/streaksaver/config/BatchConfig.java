package eu.alessandropinna.streaksaver.config;

import eu.alessandropinna.streaksaver.batch.*;
import eu.alessandropinna.streaksaver.domain.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Configuration
public class BatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    UsersReader reader;

    @Bean
    public Job jobStreaksaver(@Qualifier("stepStreaksaver") Step step) {
        return jobBuilderFactory.get("job")
                .start(step)
                .build();
    }

    @Bean
    public Step stepStreaksaver(StreaksaverProcessor processor, StreaksaverWriter writer) {
        return stepBuilderFactory.get("step")
                .<User, User>chunk(1)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Job jobEncrypt(@Qualifier("stepEncrypt") Step step) {
        return jobBuilderFactory.get("job")
                .start(step)
                .build();
    }

    @Bean
    public Step stepEncrypt(EncryptionProcessor processor, UsersWriter writer) {
        return stepBuilderFactory.get("step")
                .<User, User>chunk(1)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public ReadWriteLock lock() {
        return new ReentrantReadWriteLock();
    }

}
