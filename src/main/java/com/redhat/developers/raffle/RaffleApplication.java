package com.redhat.developers.raffle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class RaffleApplication {

    public static void main(String[] args) {
        SpringApplication.run(RaffleApplication.class, args);
    }
}
