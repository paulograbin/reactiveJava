package com.paulograbin.learningspringboot.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.converter.CompositeMessageConverterFactory;
import org.springframework.context.annotation.Bean;


@SpringCloudApplication
@EnableEurekaClient
public class ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Bean
    CompositeMessageConverterFactory teste() {
        return new CompositeMessageConverterFactory();
    }
}
