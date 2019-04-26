package com.hgy;

import com.hgy.mysqloptpess.dao.UserDao;
import com.hgy.mysqloptpess.model.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    /**
     * 根据用户id获取用户信息
     */
    @Test
    public void getUserTest(){
        UserEntity userEntity = userDao.selectByPrimaryKey(1L);
        System.out.println(userEntity);
    }

    /**
     * 根据版本号更新数据
     */
    @Test
    public void updateByVersionTest(){
        UserEntity userEntity = userDao.selectByPrimaryKey(1L);
        userEntity.setAge(20);
        userEntity.setTime(null);
        int num = userDao.updateOptimisticLockByVersion(userEntity);
        System.out.println(num);
    }

    /**
     * 根据时间戳更新数据
     *
     * 1. 和版本号的区别，值没有发生变化时间戳是不会更新的
     * 2. 1毫秒更新多次情况无法保证锁有效。
     */
    @Test
    public void updateByTimeTest(){
        UserEntity userEntity = userDao.selectByPrimaryKey(1L);
        userEntity.setAge(20);
        System.out.println(userEntity.getTime());
        int num = userDao.updateOptimisticLockByTime(userEntity);
        System.out.println(num);
    }

    /**
     * 悲观锁更新
     */
    @Test
    public void updatePessimisticTest(){
        UserEntity userEntity = userDao.selectByPrimaryKey(1L);
        userEntity.setAge(10);
        System.out.println(userEntity.getTime());
        int num = userDao.updateOptimisticLockByTime(userEntity);
        System.out.println(num);
    }

    @Test
    public void test(){
        Date date = new Date();
        System.out.println(date);
    }
}