package eu.alessandropinna.streaksaver.repository;

import eu.alessandropinna.streaksaver.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
