package eu.alessandropinna.streaksaver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String distinctId;

    private String identifier;

    private String password;

    private String landingUrl;

    private String lastReferrer;
}
