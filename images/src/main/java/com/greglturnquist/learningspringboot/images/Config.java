package com.greglturnquist.learningspringboot.images;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class Config {

    /**
     * @Configuration marks this as a configuration class containing bean definitions.
     * Since it's located underneath LearningSpringBootImagesApplication, it will be
     * automatically picked up by component scanning.
     *
     * @Bean marks the restTemplate() method as a bean definition.
     * The restTemplate() method returns a plain old Spring RestTemplate instance.
     *
     * @LoadBalanced instructs Netflix Ribbon to wrap this RestTemplate bean with load
     * balancing advice.
     */
    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
