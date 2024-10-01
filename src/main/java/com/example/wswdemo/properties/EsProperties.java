package com.example.wswdemo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "wsw.es-demo")
public class EsProperties {
    private String HOST;
}
