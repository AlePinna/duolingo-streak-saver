package ap.streaksaver.controller;

import ap.streaksaver.service.DeletionService;
import ap.streaksaver.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private DeletionService deletionService;

    @PostMapping("/saveAccount")
    public ResponseEntity<String> save(@RequestParam String identifier, @RequestParam String password) {

        return loginService.loginAndSaveUser(identifier.trim(), password.trim());
    }

    @PostMapping("/deleteAccount")
    public ResponseEntity<String> delete(@RequestParam String identifier, @RequestParam String password) {

        return deletionService.deleteUser(identifier.trim(), password.trim());
    }


}