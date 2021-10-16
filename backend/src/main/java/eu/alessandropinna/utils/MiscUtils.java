package eu.alessandropinna.utils;

import org.springframework.http.ResponseEntity;

public interface MiscUtils {

    static String getJwtHeader(ResponseEntity responseEntity) {
        return responseEntity.getHeaders().getFirst("jwt");
    }

    static String getUserIdHeader(ResponseEntity responseEntity) {
        return responseEntity.getHeaders().getFirst("x-uid");
    }
}
