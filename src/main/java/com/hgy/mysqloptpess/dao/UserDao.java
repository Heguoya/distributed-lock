package com.hgy.mysqloptpess.dao;

import com.hgy.mysqloptpess.model.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author heguoya
 * @version 1.0
 * @className UserDao
 * @description user dao
 * @date 2019/4/23 21:15
 */
@Repository
@Mapper
public interface UserDao {
    /**
     *  select user entity by primary key
     * @param id
     * @return
     */
    UserEntity selectByPrimaryKey(Long id);

    /**
     * 根据版本更新接口
     * @param userEntity
     * @return
     */
    int updateOptimisticLockByVersion(UserEntity userEntity);

    /**
     * 根据时间更新接口
     * @param userEntity
     * @return
     */
    int updateOptimisticLockByTime(UserEntity userEntity);


    /**
     *  select user entity by primary key
     * @param id
     * @return
     */
    UserEntity select(Long id);

    /**
     * 悲观锁更新接口
     * @param userEntity
     * @return
     */
    int updatePessimisticLock(UserEntity userEntity);
}
