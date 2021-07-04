package ap.streaksaver.service;

import ap.streaksaver.domain.User;
import ap.streaksaver.dto.LoginRequest;
import ap.streaksaver.dto.UserInfoResponse;
import ap.streaksaver.repository.UserRepository;
import ap.streaksaver.utils.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.UUID;

@Service
public class LoginService extends ApiClientService {

    @Autowired
    private UserRepository userRepository;

    public LoginService (@Value("${duolingo.api.login-url}") String apiUrl){
        super(apiUrl);
    }

    public ResponseEntity<UserInfoResponse> doLogin(String identifier, String password) {

        var loginRequest = LoginRequest.builder()
                .distinctId(UUID.randomUUID().toString())
                .identifier(identifier)
                .password(password)
                .landingUrl(getHomeUrl())
                .lastReferrer(getHomeUrl())
                .build();

        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<LoginRequest>(loginRequest, httpHeaders);
        return this.postForEntity(getUrl(), httpEntity, UserInfoResponse.class);
    }

    public ResponseEntity<String> loginAndSaveUser(String identifier, String password) {

        if (ObjectUtils.isEmpty(identifier) || ObjectUtils.isEmpty(password)) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> userOpt = userRepository.findById(identifier);
        boolean saveUser = !(userOpt.isPresent()
            && password.equals(userOpt.get().getPassword())
            && userOpt.get().isActive()
            && userOpt.map(User::getUserId).isPresent());

        ResponseEntity<UserInfoResponse> responseEntity = doLogin(identifier, password);

        if (responseEntity.getStatusCode().is2xxSuccessful() && saveUser) {

            String userId = userOpt.map(User::getUserId).orElse(null);
            if (userId == null){
                var userInfo = responseEntity.getBody();
                userId = userInfo != null && userInfo.getTrackingProperties() != null ?
                        userInfo.getTrackingProperties().getUserId() : null;
            }

            userRepository.save(User.builder()
                    .email(identifier)
                    .password(password)
                    .active(true)
                    .userId(userId)
                    .build());
        }

        String body = responseEntity.getStatusCode().is2xxSuccessful()
                ? "Your access token: " + MiscUtils.getJwtHeader(responseEntity)
                : "Could not login. Make sure your credentials are correct.";

        return ResponseEntity.status(responseEntity.getStatusCode()).body(body);
    }
}
