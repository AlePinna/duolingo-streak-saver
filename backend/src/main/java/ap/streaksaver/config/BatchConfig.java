package ap.streaksaver.config;

import ap.streaksaver.batch.UsersProcessor;
import ap.streaksaver.batch.UsersReader;
import ap.streaksaver.batch.UsersWriter;
import ap.streaksaver.domain.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    UsersReader reader;

    @Autowired
    UsersProcessor processor;

    @Autowired
    UsersWriter writer;

    @Bean
    public Job job(Step step) {
        return jobBuilderFactory.get("job")
                .start(step)
                .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step")
                .<User, User> chunk(1)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

}
