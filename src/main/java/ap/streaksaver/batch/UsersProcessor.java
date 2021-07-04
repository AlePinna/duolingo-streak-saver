package ap.streaksaver.batch;

import ap.streaksaver.domain.User;
import ap.streaksaver.dto.UserInfoResponse;
import ap.streaksaver.repository.UserRepository;
import ap.streaksaver.service.LoginService;
import ap.streaksaver.service.UserInfoService;
import ap.streaksaver.utils.MiscUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UsersProcessor implements ItemProcessor<User, User> {

    @Autowired
    LoginService loginService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserRepository userRepository;

    @Override
    public User process(User user) throws Exception {

        ResponseEntity<UserInfoResponse> loginResponse = loginService.doLogin(user.getEmail(), user.getPassword());

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
        return user;
    }


}


