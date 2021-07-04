package ap.streaksaver.controller;

import ap.streaksaver.repository.UserRepository;
import ap.streaksaver.service.LoginService;
import ap.streaksaver.utils.PasswordUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@RestController
@RequestMapping("/test")
class TestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginService loginService;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    @GetMapping("/hash")
    public ResponseEntity<String> hash(@RequestParam String password) {
        try {
            return ResponseEntity.ok(PasswordUtils.hashNewPassword(password).getSecond());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Hashing error");
        }
    }

    @GetMapping("/login")
    public ResponseEntity login(@RequestParam String identifier, @RequestParam String password) throws NoSuchAlgorithmException {

        ResponseEntity responseEntity = loginService.doLogin(identifier, password);

        return responseEntity;
    }

    @Scheduled(cron="${batch.cron-exp}")
    @GetMapping("/batch")
    public void login() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        jobLauncher.run(job, new JobParameters());
    }


}