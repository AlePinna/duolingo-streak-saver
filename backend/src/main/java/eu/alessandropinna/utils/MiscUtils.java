package eu.alessandropinna.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

public interface MiscUtils {

    static String getJwtHeader(ResponseEntity responseEntity) {
        return responseEntity.getHeaders().getFirst("jwt");
    }

    static String getUserIdHeader(ResponseEntity responseEntity) {
        return responseEntity.getHeaders().getFirst("x-uid");
    }

    static String removePadding(String input){
        if (ObjectUtils.isEmpty(input))
            return input;
        return input.trim();
    }
}
