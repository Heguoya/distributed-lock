package com.hgy.distributed.lock.mysql.utils;

import com.hgy.distributed.lock.DistributeLock;
import com.hgy.distributed.lock.mysql.model.ResourceLockEntity;
import com.hgy.distributed.lock.mysql.service.ResourceLockService;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.locks.LockSupport;

/**
 * @author heguoya
 * @version 1.0
 * @className MysqlLockUtil
 * @description mysql 分布式锁
 * @date 2019/4/25 20:27
 */
@Slf4j
public class MysqlDistributedLock implements DistributeLock {

    ThreadLocal<ResourceLockEntity> threadLocal = new ThreadLocal<>();
    private final ResourceLockService lockService;

    public MysqlDistributedLock(ResourceLockService lockService) {
        this.lockService = lockService;
    }

    private ResourceLockEntity getResourceLockEntity() {
        ResourceLockEntity lockEntity = new ResourceLockEntity();
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String name = Thread.currentThread().getId() + Thread.currentThread().getName();
            String hostAddress = addr.getHostAddress();
            lockEntity.setResourceName(name + hostAddress);
            lockEntity.setNodeInfo(hostAddress);
            lockEntity.setDescription(name + hostAddress);
        } catch (UnknownHostException e) {
            log.error(e.toString());
        }
        return lockEntity;
    }

    /**
     * 加锁
     *
     * @return
     */
    @Override
    public boolean lock() {
        ResourceLockEntity resourceLockEntity = getResourceLockEntity();
        threadLocal.set(resourceLockEntity);
        while (true) {
            if (lockService.getLock(resourceLockEntity)) {
                return true;
            }
            // 休息5ms
            LockSupport.parkNanos(1000 * 1000 * 5L);
        }
    }

    /**
     * 尝试获取锁
     *
     * @return
     */
    @Override
    public boolean tryLock() {
        ResourceLockEntity resourceLockEntity = getResourceLockEntity();
        threadLocal.set(resourceLockEntity);
        return lockService.getLock(resourceLockEntity);
    }

    /**
     * 尝试获取锁
     *
     * @param key
     * @return
     */
    @Override
    public boolean tryLock(String key) {
        ResourceLockEntity resourceLockEntity = getResourceLockEntity();
        resourceLockEntity.setResourceName(key);
        threadLocal.set(resourceLockEntity);
        return lockService.getLock(resourceLockEntity);
    }

    /**
     * 尝试获取锁
     *
     * @param key                锁key
     * @param acquireTimeoutInMS 轮训时间间隔
     * @param retry              重试次数
     * @return
     */
    @Override
    public boolean tryLock(String key, long acquireTimeoutInMS, int retry) {
        ResourceLockEntity resourceLockEntity = getResourceLockEntity();
        resourceLockEntity.setResourceName(key);
        threadLocal.set(resourceLockEntity);
        for (int i = 0; i < retry; i++) {
            if (lockService.getLock(resourceLockEntity)) {
                return true;
            }
            try {
                Thread.sleep(acquireTimeoutInMS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("tryLock interrupted", e);
                return false;
            }
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @return
     */
    @Override
    public boolean unLock() {
        return lockService.unLock(threadLocal.get());
    }

    /**
     * 根据key释放锁
     *
     * @param key
     * @return
     */
    @Override
    public boolean unLock(String key) {
        ResourceLockEntity byResourceName = lockService.getByResourceName(key);
        return lockService.unLock(byResourceName);
    }
}
