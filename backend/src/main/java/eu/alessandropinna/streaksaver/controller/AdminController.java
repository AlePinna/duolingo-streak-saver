package eu.alessandropinna.streaksaver.controller;

import eu.alessandropinna.utils.MiscUtils;
import eu.alessandropinna.utils.PasswordUtils;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@RestController
@RequestMapping("/admin")
@ApiIgnore
class AdminController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("jobStreaksaver")
    private Job jobStreaksaver;

    @Autowired
    @Qualifier("jobEncrypt")
    private Job jobEncrypt;

    @Autowired
    private PasswordUtils passwordUtils;

    private long jobId = 0;

    @Scheduled(cron = "${batch.cron-exp}")
    @GetMapping("/streaksaverBatch")
    public ResponseEntity streaksaverBatch() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        jobLauncher.run(jobStreaksaver, new JobParameters());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/encryptBatch")
    public ResponseEntity encryptBatch(@RequestParam(required = true) String key64, @RequestParam(required = false) String oldKey64) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        key64 = MiscUtils.removePadding(key64);
        oldKey64 = MiscUtils.removePadding(oldKey64);

        if (!Objects.equals(passwordUtils.key64, key64)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString("oldKey64", oldKey64);
        jobParametersBuilder.addLong("jobId", ++jobId);
        jobLauncher.run(jobEncrypt, jobParametersBuilder.toJobParameters());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //@PostMapping("/encrypt")
    public String encrypt(@RequestParam String text) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        return passwordUtils.encrypt(text);
    }

    //@PostMapping("/decrypt")
    public String decrypt(@RequestParam String text) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        return passwordUtils.decrypt(text);
    }


}