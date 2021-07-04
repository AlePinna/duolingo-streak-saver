package ap.streaksaver.controller;

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

    @PostMapping("/loginAndSave")
    public ResponseEntity<String> login(@RequestParam String identifier, @RequestParam String password) {

        return loginService.loginAndSaveUser(identifier.trim(), password.trim());
    }


}