package eu.alessandropinna.streaksaver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

    private TrackingProperties trackingProperties;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrackingProperties {

        private Integer streak;

        @JsonProperty("has_item_streak_freeze")
        private Boolean hasItemStreakFreeze;

        private Integer lingots;

        @JsonProperty("learning_language")
        private String learningLanguage;

        @JsonProperty("user_id")
        private String userId;
    }
}
