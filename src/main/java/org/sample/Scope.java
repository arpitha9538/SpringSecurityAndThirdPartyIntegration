package org.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.sample")
public class Scope {
    public static void main(String[] args) {
        SpringApplication.run(Scope.class, args);
    }
}