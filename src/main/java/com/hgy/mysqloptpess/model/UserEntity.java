package com.hgy.mysqloptpess.model;

import lombok.Data;

import java.util.Date;

/**
 * @author heguoya
 * @version 1.0
 * @className UserEntity
 * @description user
 * @date 2019/4/23 20:35
 */
@Data
public class UserEntity {
    /**
     * primary key
     */
    private Long id;
    /**
     * name
     */
    private String name;
    /**
     *  age
     */
    private Integer age;
    /**
     * version
     */
    private Long version;
    /**
     * timestamp
     */
    private Date time;
}
