package com.greglturnquist.learningspringboot.comments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class LearningSpringBootApplication {

    /**
     * @SpringCloudApplication replaces the previous @SpringBootApplication. This new
     * annotation extends @SpringBootApplication, giving us the same autoconfiguration,
     * component scanning, and property support (among other things) that we have
     * come to love. Additionally, it adds @EnableDiscoveryClient to register with Eureka
     * and @EnableCircuitBreaker so we can create fallback commands if a remote
     * service is down (something we'll see explored later in this chapter).
     */

    public static void main(String[] args) {
        SpringApplication.run(LearningSpringBootApplication.class, args);
    }

//	@Bean
//	ParameterMessageInterpolator parameterMessageInterpolator() {
//		return new ParameterMessageInterpolator();
//	}

}
