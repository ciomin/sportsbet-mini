package dev.sportsbetmini.betservice;

import org.springframework.boot.SpringApplication;

public class TestBetserviceApplication {

    public static void main(String[] args) {
        SpringApplication.from(BetserviceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
