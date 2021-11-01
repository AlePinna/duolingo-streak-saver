package eu.alessandropinna.streaksaver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("users-develop")
public class User {

    @Id
    private String email;

    private String password;

    private String userId;

    private boolean active;

    private boolean encrypted;

    private String keyHash;

    private String hashSalt;

    @Transient
    private boolean buyStreakFreeze;

    @Transient
    private String jwt;

    @Transient
    private String learningLanguage;

}
