package com.hgy.mysqloptpess.service.impl;

import com.hgy.mysqloptpess.dao.UserDao;
import com.hgy.mysqloptpess.model.UserEntity;
import com.hgy.mysqloptpess.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author heguoya
 * @version 1.0
 * @className UserServiceImpl
 * @description
 * @date 2019/4/25 20:01
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public UserEntity getUser(Long id) {
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(UserEntity userEntity) throws Exception {
        // 添加悲观锁
        UserEntity userEntity1 = userDao.select(userEntity.getId());
        if (userEntity1 == null) {
            throw new Exception("参数错误");
        }
        BeanUtils.copyProperties(userEntity, userEntity1);
        // 更新完成释放锁
        return userDao.updatePessimisticLock(userEntity1);
    }
}
