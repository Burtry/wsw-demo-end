package com.example.wswdemo.initializer;

import com.example.wswdemo.config.QuartzConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SchedulerInitializer {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private QuartzConfig quartzConfig;

    @PostConstruct
    public void init() throws SchedulerException {
        scheduler.scheduleJob(quartzConfig.updateStatusJobDetail(), quartzConfig.updateStatusJobTrigger());
        log.info("定时任务开启!");
        scheduler.start();
    }

}
