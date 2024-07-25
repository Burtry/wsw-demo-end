package com.example.wswdemo;


import com.example.wswdemo.job.UpdateStatus;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class WswDemoApplicationTests {

    @Autowired
    private Scheduler scheduler;

    @Test
    void testQuartz() throws SchedulerException, InterruptedException {

        //2.创建JobDetail实例
        JobDetail jobDetail = JobBuilder.newJob(UpdateStatus.class)
                .withIdentity("textJob").build();

        //3.构建Trigger实例,每隔一分钟执行一次
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger")
                .startNow() //立即开始
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(1)   //每隔一分钟执行
                        .repeatForever()).build();  //一直执行

        //执行
        scheduler.scheduleJob(jobDetail, trigger);
        System.out.println("开始");
        scheduler.start();

        TimeUnit.HOURS.sleep(1);
        scheduler.shutdown();
    }

}
