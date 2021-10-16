package eu.alessandropinna.streaksaver.batch;

import eu.alessandropinna.streaksaver.domain.User;
import eu.alessandropinna.streaksaver.dto.UserInfoResponse;
import eu.alessandropinna.streaksaver.repository.UserRepository;
import eu.alessandropinna.streaksaver.service.LoginService;
import eu.alessandropinna.streaksaver.service.UserInfoService;
import eu.alessandropinna.utils.MiscUtils;
import eu.alessandropinna.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StreaksaverProcessor implements ItemProcessor<User, User> {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordUtils passwordUtils;

    @Override
    public User process(User user) {

        try {

            String decryptedPassword = passwordUtils.decrypt(user.getPassword());
            ResponseEntity<UserInfoResponse> loginResponse = loginService.doLogin(user.getEmail(), decryptedPassword);

            if (!loginResponse.getStatusCode().is2xxSuccessful()) {

                log.error("Error during login for user {} - Status code {}", user.getEmail(), loginResponse.getStatusCodeValue());
                if (loginResponse.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                    user.setActive(false);
                    userRepository.save(user);
                }
                return user;
            }

            UserInfoResponse.TrackingProperties trackingProperties = loginResponse.getBody() != null
                    ? loginResponse.getBody().getTrackingProperties() : null;

            if (trackingProperties != null && !trackingProperties.getHasItemStreakFreeze()
                    && trackingProperties.getStreak() > 0 && trackingProperties.getLingots() >= 10) {

                user.setLearningLanguage(trackingProperties.getLearningLanguage());
                user.setBuyStreakFreeze(true);
                user.setJwt(MiscUtils.getJwtHeader(loginResponse));

            } else {
                log.info("User {} can't buy a streak right now", user.getEmail());
            }

        } catch (Exception e) {
            log.error(Strings.EMPTY, e);
        }

        return user;

    }


}


