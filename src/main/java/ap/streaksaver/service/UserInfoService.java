package ap.streaksaver.service;

import ap.streaksaver.domain.User;
import ap.streaksaver.dto.LoginRequest;
import ap.streaksaver.dto.UserInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService extends ApiClientService {

    public UserInfoService(@Value("${duolingo.api.user-info-url}") String apiUrl) {
        super(apiUrl);
    }

    public ResponseEntity<UserInfoResponse> getUserInfo(User user) {

        var httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(user.getJwt());
        var httpEntity = new HttpEntity<LoginRequest>(httpHeaders);
        return this.postForEntity(getUrl("userId", user.getUserId()), httpEntity, UserInfoResponse.class);
    }
}
