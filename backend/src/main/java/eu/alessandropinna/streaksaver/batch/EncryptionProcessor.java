package eu.alessandropinna.streaksaver.batch;

import eu.alessandropinna.streaksaver.domain.User;
import eu.alessandropinna.streaksaver.repository.UserRepository;
import eu.alessandropinna.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
@Component
@Scope(value = "step")
public class EncryptionProcessor implements ItemProcessor<User, User> {

    private String oldKey64;

    private SecretKey oldSecretKey;

    @Autowired
    private PasswordUtils passwordUtils;

    @Autowired
    UserRepository userRepository;

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        JobParameters jobParameters = stepExecution.getJobParameters();
        oldKey64 = jobParameters.getString("oldKey64");
        if (oldKey64 != null) {
            byte[] decodedKey = Base64.getDecoder().decode(oldKey64);
            oldSecretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        } else {
            oldSecretKey = null;
        }
    }

    @Override
    public User process(User user) {

        try {

            String newEncryptedPassword;

            if (oldKey64 != null && user.isEncrypted() && passwordUtils
                    .isHashMatching(oldKey64, user.getKeyHash(), user.getHashSalt())) {
                String decryptedPassword = passwordUtils.decrypt(user.getPassword(), oldSecretKey);
                newEncryptedPassword = passwordUtils.encrypt(decryptedPassword);
            }
            else if (!user.isEncrypted()) {
                newEncryptedPassword = passwordUtils.encrypt(user.getPassword());
            }
            else {
                return null;
            }

            user.setPassword(newEncryptedPassword);
            user.setHashSalt(passwordUtils.hashPair.getFirst());
            user.setKeyHash(passwordUtils.hashPair.getSecond());
            user.setEncrypted(true);
            userRepository.save(user);


            return user;

        } catch (Exception e) {
            log.error(Strings.EMPTY, e);
            return null;
        }
    }


}


