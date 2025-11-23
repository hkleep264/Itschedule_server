package com.itschedule.itschedule_server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.itschedule.itschedule_server.repository")
public class ItscheduleServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItscheduleServerApplication.class, args);
    }

}
