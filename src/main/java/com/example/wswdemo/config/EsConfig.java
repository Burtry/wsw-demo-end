package com.example.wswdemo.config;

import com.example.wswdemo.properties.EsProperties;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsConfig {

    @Autowired
    private EsProperties esProperties;

    @Bean
    public RestHighLevelClient client(){
        return new RestHighLevelClient(RestClient.builder(
                HttpHost.create(esProperties.getHOST())
        ));
    }


}
