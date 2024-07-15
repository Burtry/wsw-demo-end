package com.example.wswdemo.config;

import com.example.wswdemo.properties.AliOssProperties;
import com.example.wswdemo.utils.AliOSSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建AliOssUntil对象
 */
@Configuration
@Slf4j
public class AliOSSConfiguration {
    @Bean
    @ConditionalOnMissingBean  //保证容器中只有这一个bean对象
    public AliOSSUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("开始创建阿里云文件上传工具类对象:{}",aliOssProperties);
        return new AliOSSUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getBucketName());
    }
}
