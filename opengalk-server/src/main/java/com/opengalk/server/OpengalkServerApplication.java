package com.opengalk.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.opengalk.server.数据访问层")
@SpringBootApplication
public class OpengalkServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpengalkServerApplication.class, args);
    }

}
