package eu.alessandropinna.streaksaver.config;

import eu.alessandropinna.streaksaver.batch.UsersProcessor;
import eu.alessandropinna.streaksaver.batch.UsersReader;
import eu.alessandropinna.streaksaver.batch.UsersWriter;
import eu.alessandropinna.streaksaver.domain.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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
