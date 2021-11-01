package eu.alessandropinna.streaksaver.batch;

import eu.alessandropinna.streaksaver.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UsersWriter implements ItemWriter<User> {

    @Override
    public void write(List<? extends User> list) {

        log.info("Password encrypted for users " + list.stream()
                .map(u -> u.getEmail()).collect(Collectors.toList()));
    }
}
