package com.example.wswdemo.config;

import com.example.wswdemo.factory.AutowiringSpringBeanJobFactory;
import com.example.wswdemo.job.PrintWordsJob;
import com.example.wswdemo.job.UpdateStatus;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@Slf4j
public class QuartzConfig {

    @Autowired
    private AutowiringSpringBeanJobFactory jobFactory;


    //创建调度器
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(jobFactory);
        return factory;
    }

    //获取调度器
    @Bean
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }

    //创建任务类
    @Bean
    public JobDetail updateStatusJobDetail() {
        return JobBuilder.newJob(UpdateStatus.class)
                .withIdentity("updateStatusJob")
                .storeDurably()
                .build();
    }


    //获取触发器
    @Bean
    public Trigger updateStatusJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(updateStatusJobDetail())
                .withIdentity("updateStatusTrigger")
                .startNow() //启动时立即触发一次
                .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?")) // 每分钟执行一次
                .build();
    }


}
