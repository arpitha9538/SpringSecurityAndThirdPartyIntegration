package org.sample;

import org.sample.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;


@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class Scope {
    public static void main(String[] args) {
        SpringApplication.run(Scope.class, args);
    }
}