package com.example.wswdemo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wsw.jwt")
@Data
public class JwtProperties {
    //生成jwt令牌的相关配置
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}
