package com.ftms;

// This is the entry point of your entire Spring Boot application.
// When you run mvn spring-boot:run, Java starts from here.
// @SpringBootApplication tells Spring to auto-configure everything and scan all packages below this

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class FtmsApplication {
    public static void main(String[] args) {
        // This line starts the embedded Tomcat server inside Spring Boot
        // No need to install separate Tomcat. Spring Boot includes it.
        SpringApplication.run(FtmsApplication.class, args);
        System.out.println("FTMS Backend started successfully on port 8080");
    }

    // RestTemplate Bean for making HTTP calls to external APIs
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
