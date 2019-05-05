package com.hgy;

import com.hgy.distributed.lock.DistributeLock;
import com.hgy.distributed.lock.mysql.service.ResourceLockService;
import com.hgy.distributed.lock.mysql.utils.MysqlDistributedLock;
import com.hgy.mysqloptpess.model.UserEntity;
import com.hgy.mysqloptpess.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author heguoya
 * @version 1.0
 * @className MysqlLock
 * @description mysql锁
 * @date 2019/4/28 14:37
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class MysqlLock {
    @Autowired
    private ResourceLockService resourceLockService;
    @Autowired
    private UserService userService;

    @Test
    public void testInsertUser() {
        DistributeLock distributeLock = new MysqlDistributedLock(resourceLockService);
        distributeLock.lock();
        UserEntity userEntity = new UserEntity();
        userEntity.setName("evan");
        userEntity.setAge(28);
        userService.insertUser(userEntity);
        distributeLock.unLock();
    }


}
