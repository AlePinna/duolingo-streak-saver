package ap.streaksaver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Document("users")
public class User {

    @Id
    private String email;

    private String password;

    private String userId;

    private boolean active;

    @Transient
    private boolean buyStreakFreeze = false;

    @Transient
    private String jwt;

    @Transient
    private String learningLanguage;

}
