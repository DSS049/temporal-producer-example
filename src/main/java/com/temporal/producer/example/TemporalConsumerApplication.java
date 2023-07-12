package com.temporal.producer.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.*", "com.maersk.*", "com.*"})
public class TemporalConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemporalConsumerApplication.class, args);
    }

}
