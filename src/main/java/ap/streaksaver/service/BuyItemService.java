package ap.streaksaver.service;

import ap.streaksaver.domain.User;
import ap.streaksaver.dto.LoginRequest;
import ap.streaksaver.dto.ShopRequest;
import ap.streaksaver.dto.UserInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BuyItemService extends ApiClientService {

    public BuyItemService(@Value("${duolingo.api.buy-item-url}") String apiUrl){
        super(apiUrl);
    }

    public ResponseEntity buyStreakFreeze(User user) {

        var shopRequest = ShopRequest.builder()
                .itemName("streak_freeze")
                .learningLanguage(user.getLearningLanguage())
                .build();

        var httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(user.getJwt());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<ShopRequest>(shopRequest, httpHeaders);
        return this.postForEntity(getUrl("userId", user.getUserId()), httpEntity, String.class);
    }
}
