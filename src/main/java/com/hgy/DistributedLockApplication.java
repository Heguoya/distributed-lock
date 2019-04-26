package com.hgy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author heguoya
 * @version 1.0
 * @className DistributedLockApplication
 * @description distributed lock
 * @date 2019/4/23 20:33
 */
@SpringBootApplication
@MapperScan("com.hgy")
public class DistributedLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedLockApplication.class, args);
    }

}
