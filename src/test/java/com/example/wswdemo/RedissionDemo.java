//package com.example.wswdemo;
//
//import org.junit.jupiter.api.Test;
//import org.redisson.api.RDelayedQueue;
//import org.redisson.api.RQueue;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class RedissionDemo {
//
//    @Autowired
//    private RedissonClient redissonClient;
//
//    @Test
//    void test() {
//        //添加消息到延迟队列
//        RQueue<Object> testQueue = redissonClient.getQueue("testQueue");
//
//        RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(testQueue);
//    }
//}
