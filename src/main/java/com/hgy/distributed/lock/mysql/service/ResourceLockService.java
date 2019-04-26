package com.hgy.distributed.lock.mysql.service;

import com.hgy.distributed.lock.mysql.model.ResourceLockEntity;

/**
 * @author heguoya
 * @version 1.0
 * @className ResourceLockService
 * @description 资源服务层
 * @date 2019/4/25 21:14
 */
public interface ResourceLockService {
    /**
     * 根据id获取资源对象
     *
     * @param id
     * @return
     */
    ResourceLockEntity get(Long id);


    /**
     * 获取锁
     *
     *
     * @param resourceName
     * @return
     */
    boolean getLock(ResourceLockEntity resourceName);


    /**
     * 获取锁
     *
     *
     * @param resourceName
     * @return
     */
    boolean unLock(ResourceLockEntity resourceName);

    /**
     * 根据资源名称获取资源对象
     *
     * @param name
     * @return
     */
    ResourceLockEntity getByResourceName(String name);

    /**
     * 更新资源锁信息
     *
     * @param resourceLockEntity
     * @return
     */
    int update(ResourceLockEntity resourceLockEntity);

    /**
     * 删除资源锁
     *
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 根据资源名称删除资源锁
     *
     * @param resourceName
     * @return
     */
    int deleteByResourceName(String resourceName);
}
