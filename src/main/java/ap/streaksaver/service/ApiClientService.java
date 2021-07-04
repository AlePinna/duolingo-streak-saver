package ap.streaksaver.service;

import com.mongodb.lang.Nullable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class ApiClientService {

    @Autowired
    private RestTemplate restTemplate;

    @Getter
    @Value("${duolingo.web-app.home-url}")
    String homeUrl;

    @Value("${duolingo.api.base-url}")
    String baseUrl;

    String apiUrl;

    public ApiClientService(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getUrl() {
        return baseUrl + apiUrl;
    }

    public String getUrl(String parameter, String value) {
        String pathParam = "{" + parameter + "}";
        return getUrl().replace(pathParam, value);
    }

    public <T> ResponseEntity<T> postForEntity(String url, @Nullable Object request, Class<T> responseType) {
        ResponseEntity<T> responseEntity;
        try {
            log.info(request.toString());
            responseEntity = restTemplate.postForEntity(url, request, responseType);
            log.info(responseEntity.toString());
        } catch (HttpStatusCodeException ex) {
            responseEntity = ResponseEntity.status(ex.getStatusCode()).headers(ex.getResponseHeaders()).build();
        }
        return responseEntity;
    }
}
