package com.greglturnquist.learningspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;


@SpringCloudApplication
public class LearningSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningSpringBootApplication.class, args);
	}

	@Bean
	HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}
}
