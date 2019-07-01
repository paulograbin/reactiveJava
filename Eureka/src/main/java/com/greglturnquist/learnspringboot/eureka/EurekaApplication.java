package com.greglturnquist.learnspringboot.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {

    /**
     * @SpringBootApplication marks this app as a Spring Boot application, which means
     * that it will autoconfigure beans based on the classpath, and load properties as
     * well.
     */
    /**
     * @EnableEurekaServer tells Spring Cloud Eureka that we want to run a Eureka
     * Server. It proceeds to configure all the necessary beans to host a service
     * registry.
     */

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }

}
