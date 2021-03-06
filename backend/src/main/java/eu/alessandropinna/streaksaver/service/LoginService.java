package eu.alessandropinna.streaksaver.service;

import eu.alessandropinna.streaksaver.domain.User;
import eu.alessandropinna.streaksaver.dto.LoginRequest;
import eu.alessandropinna.streaksaver.dto.UserInfoResponse;
import eu.alessandropinna.streaksaver.repository.UserRepository;
import eu.alessandropinna.utils.MiscUtils;
import eu.alessandropinna.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoginService extends ApiClientService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordUtils passwordUtils;

    public LoginService(@Value("${duolingo.api.login-url}") String apiUrl) {
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

    public ResponseEntity<String> loginAndSaveUser(String identifier, String password) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        if (ObjectUtils.isEmpty(identifier) || ObjectUtils.isEmpty(password)) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> userOpt = userRepository.findById(identifier);

        ResponseEntity<UserInfoResponse> responseEntity = doLogin(identifier, password);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {

            String userId = userOpt.map(User::getUserId).orElse(null);
            if (userId == null) {
                var userInfo = responseEntity.getBody();
                userId = userInfo != null && userInfo.getTrackingProperties() != null ?
                        userInfo.getTrackingProperties().getUserId() : null;
            }

            userRepository.save(User.builder()
                    .email(identifier)
                    .password(passwordUtils.encrypt(password))
                    .hashSalt(passwordUtils.hashPair.getFirst())
                    .keyHash(passwordUtils.hashPair.getSecond())
                    .encrypted(true)
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
