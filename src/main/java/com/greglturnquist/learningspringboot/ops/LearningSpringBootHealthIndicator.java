package com.greglturnquist.learningspringboot.ops;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


@Component
public class LearningSpringBootHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        try {
            URL url = null;
            url = new URL("http://greglturnquist.com/books/learning-spring-boot");

            final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            final int statusCode = urlConnection.getResponseCode();
            if (statusCode >= 200 && statusCode < 300) {
                return Health.up().build();
            } else {
                return Health.down().withDetail("HTTP status code", statusCode).build();
            }
        } catch (IOException e) {
            return Health.down(e).build();
        }
    }
}
