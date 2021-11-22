package eu.alessandropinna.streaksaver.controller;

import eu.alessandropinna.streaksaver.service.DeletionService;
import eu.alessandropinna.streaksaver.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.locks.ReadWriteLock;

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private DeletionService deletionService;

    @Autowired
    private ReadWriteLock lock;

    @PostMapping("/saveAccount")
    public ResponseEntity<String> save(@RequestParam String identifier, @RequestParam String password) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        try {
            lock.writeLock().lock();
            return loginService.loginAndSaveUser(identifier.trim(), password.trim());
        } finally {
            lock.writeLock().unlock();
        }
    }

    @PostMapping("/deleteAccount")
    public ResponseEntity<String> delete(@RequestParam String identifier, @RequestParam String password) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        try {
            lock.writeLock().lock();
            return deletionService.deleteUser(identifier.trim(), password.trim());
        } finally {
            lock.writeLock().unlock();
        }
    }


}