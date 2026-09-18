package com.leadback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class LeadbackApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeadbackApplication.class, args);
    }
}
