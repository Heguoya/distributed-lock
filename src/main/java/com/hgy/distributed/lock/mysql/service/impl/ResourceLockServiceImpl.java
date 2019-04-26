package com.hgy.distributed.lock.mysql.service.impl;

import com.hgy.distributed.lock.mysql.dao.ResourceLockDao;
import com.hgy.distributed.lock.mysql.model.ResourceLockEntity;
import com.hgy.distributed.lock.mysql.service.ResourceLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author heguoya
 * @version 1.0
 * @className ResourceLockServiceImpl
 * @description
 * @date 2019/4/25 22:04
 */
@Service
public class ResourceLockServiceImpl implements ResourceLockService {
    @Autowired
    private ResourceLockDao resourceLockDao;

    @Override
    public ResourceLockEntity get(Long id) {
        return null;
    }


    /**
     * 获取锁
     *
     * @param resourceName
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean getLock(ResourceLockEntity resourceName) {
        ResourceLockEntity byResourceName = resourceLockDao.getByResourceName(resourceName.getResourceName());
        if (byResourceName == null) {
            int insert = resourceLockDao.insert(resourceName);
            if (insert > 0) {
                return true;
            }
            return false;
        } else {
            if (byResourceName.getNodeInfo().equals(resourceName.getNodeInfo())) {
                int i = resourceLockDao.updateByResourceNameEnter(byResourceName);
                if (i > 0) {
                    return true;
                }
                return false;
            } else {
                return false;
            }
        }
    }

    /**
     * 释放锁
     *
     * @param resourceName
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unLock(ResourceLockEntity resourceName) {
        ResourceLockEntity byResourceName = resourceLockDao.getByResourceName(resourceName.getResourceName());
        if (byResourceName == null) {
            return false;
        }else {
            if (resourceName.getNodeInfo().equals(byResourceName.getNodeInfo())) {
                int i = resourceLockDao.updateByResourceNameOut(byResourceName);
            }
        }
        return false;
    }


    @Override
    public ResourceLockEntity getByResourceName(String name) {
        return null;
    }

    @Override
    public int update(ResourceLockEntity resourceLockEntity) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    @Override
    public int deleteByResourceName(String resourceName) {
        return 0;
    }
}
