package com.hz.platform.master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class HzPlatformMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzPlatformMasterApplication.class, args);
    }

}
