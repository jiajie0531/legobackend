package com.delllogistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableCaching
@EnableTransactionManagement
@SpringBootApplication
public class DelllogisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DelllogisticsApplication.class, args);
    }

}
