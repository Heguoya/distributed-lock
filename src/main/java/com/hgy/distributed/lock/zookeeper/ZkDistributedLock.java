package com.hgy.distributed.lock.zookeeper;

import com.hgy.distributed.lock.DistributeLock;
import lombok.extern.slf4j.Slf4j;

/**
 * @author heguoya
 * @version 1.0
 * @className ZkDistributedLock
 * @description zk实现分布式锁
 * @date 2019/5/5 17:18
 */
@Slf4j
public class ZkDistributedLock implements DistributeLock {
    /**
     * 加锁
     *
     * @return
     */
    @Override
    public boolean lock() {
        return false;
    }

    /**
     * 尝试获取锁
     *
     * @return
     */
    @Override
    public boolean tryLock() {
        return false;
    }

    /**
     * 尝试获取锁
     *
     * @param key
     * @return
     */
    @Override
    public boolean tryLock(String key) {
        return false;
    }

    /**
     * 尝试获取锁
     *
     * @param key                锁key
     * @param acquireTimeoutInMS 获取锁等待时间
     * @param retry              重试次数
     * @return
     */
    @Override
    public boolean tryLock(String key, long acquireTimeoutInMS, int retry) {
        return false;
    }

    /**
     * 释放锁
     *
     * @return
     */
    @Override
    public boolean unLock() {
        return false;
    }

    /**
     * 根据key释放锁
     *
     * @param key
     * @return
     */
    @Override
    public boolean unLock(String key) {
        return false;
    }
}
