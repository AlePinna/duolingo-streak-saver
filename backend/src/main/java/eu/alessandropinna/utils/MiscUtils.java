package eu.alessandropinna.utils;

import org.springframework.http.ResponseEntity;

public interface MiscUtils {

    public static String getJwtHeader(ResponseEntity responseEntity) {
        return responseEntity.getHeaders().getFirst("jwt");
    }

    public static String getUserIdHeader(ResponseEntity responseEntity) {
        return responseEntity.getHeaders().getFirst("x-uid");
    }
}
