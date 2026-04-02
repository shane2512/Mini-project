package com.ftms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FtmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(FtmsApplication.class, args);
        System.out.println("FTMS Backend started successfully on port 8080");
    }
}
