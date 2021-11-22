package eu.alessandropinna.streaksaver.controller;

import eu.alessandropinna.utils.MiscUtils;
import eu.alessandropinna.utils.PasswordUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;

@RestController
@RequestMapping("/admin")
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

    @Autowired
    private ReadWriteLock lock;

    private long jobId = 0;

    @Scheduled(cron = "${batch.cron-exp}")
    public void streaksaverBatch() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        try {
            lock.writeLock().lock();
            jobLauncher.run(jobStreaksaver, new JobParameters());
        }
        finally {
            lock.writeLock().unlock();
        }

    }

    @PostMapping("/streaksaverBatch")
    public ResponseEntity streaksaverBatch(@RequestParam(required = true) String key64) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        key64 = MiscUtils.removePadding(key64);

        if (!Objects.equals(passwordUtils.key64, key64)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        streaksaverBatch();

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

        try {
            lock.writeLock().lock();
            jobLauncher.run(jobEncrypt, jobParametersBuilder.toJobParameters());
        } finally {
            lock.writeLock().unlock();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }


}