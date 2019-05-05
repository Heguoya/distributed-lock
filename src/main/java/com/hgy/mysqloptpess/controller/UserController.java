package com.hgy.mysqloptpess.controller;

import com.hgy.mysqloptpess.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author heguoya
 * @version 1.0
 * @className UserController
 * @description 用户控制层
 * @date 2019/4/28 16:01
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;
}
