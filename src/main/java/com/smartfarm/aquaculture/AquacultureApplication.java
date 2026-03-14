package com.smartfarm.aquaculture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableKafka
@EnableAsync
@EnableScheduling
public class AquacultureApplication {
    public static void main(String[] args) {
        SpringApplication.run(AquacultureApplication.class, args);
    }
}
