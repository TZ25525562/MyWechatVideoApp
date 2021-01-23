package com.tz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;


@SpringBootApplication
//@ComponentScans(value = {@ComponentScan("com.tz.*")})
public class MyvideoappApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyvideoappApplication.class, args);
    }

}
