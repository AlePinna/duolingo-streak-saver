package eu.alessandropinna.streaksaver.batch;

import eu.alessandropinna.streaksaver.domain.User;
import eu.alessandropinna.streaksaver.repository.UserRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class UsersReader implements ItemReader<User> {

    @Autowired
    UserRepository userRepository;

    private Iterator<User> usersIterator;

    private void initialize() {
        List<User> users = userRepository.findAll();
        usersIterator = users.iterator();
    }

    @Override
    public User read() {
        User user = null;
        if (usersIterator == null) {
            initialize();
        }
        if (usersIterator.hasNext()){
            user = usersIterator.next();
        } else {
            usersIterator = null;
        }
        return user;
    }
}
