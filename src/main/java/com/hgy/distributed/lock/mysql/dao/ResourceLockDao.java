package com.hgy.distributed.lock.mysql.dao;

import com.hgy.distributed.lock.mysql.model.ResourceLockEntity;
import org.springframework.stereotype.Repository;

/**
 * @author heguoya
 * @version 1.0
 * @className ResourceLockDao
 * @description 资源锁
 * @date 2019/4/25 21:08
 */
@Repository
public interface ResourceLockDao {

    /**
     * 插入数据
     * @param resourceLockEntity
     * @return
     */
    int insert(ResourceLockEntity resourceLockEntity);



    /**
     * 根据id获取资源对象
     *
     * @param id
     * @return
     */
    ResourceLockEntity get(Long id);

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
     * 更新资源锁信息
     *
     * @param resourceLockEntity
     * @return
     */
    int updateByResourceNameEnter(ResourceLockEntity resourceLockEntity);

    /**
     * 更新资源锁信息
     *
     * @param resourceLockEntity
     * @return
     */
    int updateByResourceNameOut(ResourceLockEntity resourceLockEntity);

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
