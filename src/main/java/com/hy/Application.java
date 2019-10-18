package com.hy;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@MapperScan("com.hy.**.mapper.**")
public class Application {

    public static void main(String[] args) {
        log.info("===========================hello=============================");
        SpringApplication.run(Application.class, args);
    }

}
