package com.hgy;

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
public class DistributedLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedLockApplication.class, args);
    }

}
