package com.hgy.mysqloptpess.service;

import com.hgy.mysqloptpess.model.UserEntity;

/**
 * @author heguoya
 * @version 1.0
 * @className UserService
 * @description 用户service
 * @date 2019/4/25 19:57
 */
public interface UserService {
    /**
     * 查询用户
     *
     * @param id
     * @return
     */
    UserEntity getUser(Long id);

    /**
     * 添加用户
     * @param userEntity
     * @return
     */
    int insertUser(UserEntity userEntity);

    /**
     * 更新用户信息
     *
     * @param userEntity
     * @return
     */
    int update(UserEntity userEntity) throws Exception;

}
