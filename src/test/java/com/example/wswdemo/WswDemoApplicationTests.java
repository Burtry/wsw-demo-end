//package com.example.wswdemo;
//
//
//import cn.hutool.core.thread.ThreadFactoryBuilder;
//import com.example.wswdemo.job.UpdateStatus;
//import org.junit.jupiter.api.Test;
//import org.quartz.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.util.comparator.Comparators;
//
//import java.util.List;
//import java.util.concurrent.*;
//import java.util.concurrent.locks.ReentrantLock;
//
////@SpringBootTest
//class WswDemoApplicationTests {
//
//    @Autowired
//    private Scheduler scheduler;
//
//    @Test
//    void testQuartz() throws SchedulerException, InterruptedException {
//
//        //2.创建JobDetail实例
//        JobDetail jobDetail = JobBuilder.newJob(UpdateStatus.class)
//                .withIdentity("textJob").build();
//
//        //3.构建Trigger实例,每隔一分钟执行一次
//        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger")
//                .startNow() //立即开始
//                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//                        .withIntervalInMinutes(1)   //每隔一分钟执行
//                        .repeatForever()).build();  //一直执行
//
//        //执行
//        scheduler.scheduleJob(jobDetail, trigger);
//        System.out.println("开始");
//        scheduler.start();
//
//        TimeUnit.HOURS.sleep(1);
//        scheduler.shutdown();
//    }
//
//
//
//    //自定义任务类
//    static class PriorityTask implements Runnable{
//
//        private final int priority;
//        private final String name;
//
//        public PriorityTask(int priority, String name) {
//            this.priority = priority;
//            this.name = name;
//        }
//
//        public int getPriority() {
//            return priority;
//        }
//
//        @Override
//        public void run() {
//            System.out.println( name + getPriority());
//        }
//    }
//
//    @Test
//    void threadPoolTest() throws ExecutionException, InterruptedException {
//
//        PriorityBlockingQueue<Runnable> priorityBlockingQueue = new PriorityBlockingQueue<>(10,(r1,r2) -> {
//            if (r1 instanceof PriorityTask && r2 instanceof PriorityTask) {
//                return ((PriorityTask) r1).getPriority() - ((PriorityTask) r2).getPriority();
//            }
//            return 0;
//        });
//
//        //公平锁
//        ReentrantLock reentrantLock = new ReentrantLock(true);
//
//
//        new ThreadFactoryBuilder().setNamePrefix("NamePrefix");
//
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 5, 2, TimeUnit.MINUTES, priorityBlockingQueue);
//
//        threadPoolExecutor.execute(new PriorityTask(2,"name"));
//        threadPoolExecutor.execute(new PriorityTask(3,"name"));
//        threadPoolExecutor.execute(new PriorityTask(1,"name"));
//        Future future = threadPoolExecutor.submit(new PriorityTask(4, "name"));
//
//        Object o = future.get();
//        System.out.println(o);
//
//        threadPoolExecutor.shutdown();
//    }
//
//
//}
