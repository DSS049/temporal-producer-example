package com.temporal.producer.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = {"com.*", "com.maersk.*", "com.*"})
public class TemporalConsumerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TemporalConsumerApplication.class, args);
        String property = context.getEnvironment().getProperty("temporal.feedback.task-queue-name");
        System.setProperty("FEED_BACK_QUEUE", property);
    }

}
