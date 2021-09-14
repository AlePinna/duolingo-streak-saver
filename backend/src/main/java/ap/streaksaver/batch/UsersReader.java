package ap.streaksaver.batch;

import ap.streaksaver.domain.User;
import ap.streaksaver.repository.UserRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

@Component
public class UsersReader implements ItemReader<User> {

    @Autowired
    UserRepository userRepository;

    private Iterator<User> usersIterator;

    @PostConstruct
    private void initialize() {
        usersIterator = userRepository.findAll(Example.of(
                User.builder().active(true).build()))
                .iterator();
    }

    @Override
    public User read() {
        var user = usersIterator.hasNext() ? usersIterator.next() : null;
        if (user == null){
            CompletableFuture.runAsync(this::initialize);
        }
        return user;
    }
}
