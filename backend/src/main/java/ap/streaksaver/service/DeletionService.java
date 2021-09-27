package ap.streaksaver.service;

import ap.streaksaver.domain.User;
import ap.streaksaver.dto.UserInfoResponse;
import ap.streaksaver.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
@Slf4j
public class DeletionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginService loginService;

    public ResponseEntity<String> deleteUser(String identifier, String password) {

        if (ObjectUtils.isEmpty(identifier) || ObjectUtils.isEmpty(password)) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> userOpt = userRepository.findById(identifier);

        if (!userOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();

        if (password.equals(user.getPassword()) || isLogged(identifier, password)) {
            userRepository.delete(userOpt.get());
            return ResponseEntity.ok("Account successfully deleted");
        } else {
            return ResponseEntity.status(401).body("Access failed, please make sure your credentials are correct");
        }
    }

    private boolean isLogged(String identifier, String password) {
        try {
            return loginService.doLogin(identifier, password).getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error(Strings.EMPTY, e);
            return false;
        }
    }

}