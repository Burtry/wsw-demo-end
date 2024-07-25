package com.example.wswdemo.initializer;

import com.example.wswdemo.config.QuartzConfig;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SchedulerInitializer implements CommandLineRunner {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private QuartzConfig quartzConfig;

    @Override
    public void run(String... args) throws SchedulerException {
        scheduler.scheduleJob(quartzConfig.updateStatusJobDetail(), quartzConfig.updateStatusJobTrigger());
        log.info("定时任务开启!");
    }
}
