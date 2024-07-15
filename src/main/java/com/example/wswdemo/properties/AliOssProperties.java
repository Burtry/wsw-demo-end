package com.example.wswdemo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wsw.alioss")
@Data
public class AliOssProperties {

    private String endpoint;
    private String bucketName;

}
