package com.hgy.distributed.lock;

/**
 * @author heguoya
 * @version 1.0
 * @className DistributedLock
 * @description 分布式锁
 * @date 2019/5/5 17:00
 */
public interface DistributeLock {
    /**
     * 加锁
     * @return
     */
   boolean lock();

    /**
     * 尝试获取锁
     * @return
     */
    boolean tryLock();

    /**
     * 尝试获取锁
     * @param key
     * @return
     */
    boolean tryLock(String key);

    /**
     * 尝试获取锁
     * @param key 锁key
     * @param acquireTimeoutInMS 获取锁等待时间
     * @param retry 重试次数
     * @return
     */
    boolean tryLock(String key, long acquireTimeoutInMS, int retry);

    /**
     * 释放锁
     * @return
     */
    boolean unLock();

    /**
     * 根据key释放锁
     * @param key
     * @return
     */
    boolean unLock(String key);
}
