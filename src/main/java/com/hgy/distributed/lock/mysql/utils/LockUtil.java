package com.hgy.distributed.lock.mysql.utils;

import com.hgy.distributed.lock.mysql.model.ResourceLockEntity;
import com.hgy.distributed.lock.mysql.service.ResourceLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.locks.LockSupport;

/**
 * @author heguoya
 * @version 1.0
 * @className LockUtil
 * @description 分布式锁工具类
 * @date 2019/4/26 10:22
 */
@Component
@Slf4j
public class LockUtil {


    @Resource
    ResourceLockService lockService;


    public void lock(ResourceLockEntity resourceLockEntity) {
        while (true) {
            if (lockService.getLock(resourceLockEntity)) {
                return;
            }
            // 休息5ms
            LockSupport.parkNanos(1000 * 1000 * 5L);
        }
    }

    public boolean tryLock(ResourceLockEntity resourceLockEntity) {
        return lockService.getLock(resourceLockEntity);
    }

    public boolean tryLock() {
        return lockService.getLock(getResourceLockEntity());
    }

    public boolean tryLock(Long time) {
        return tryLock(getResourceLockEntity(), time);
    }

    public boolean tryLock(ResourceLockEntity resourceLockEntity, Long time) {
        Long startTime = System.currentTimeMillis();
        while (true) {
            if (lockService.getLock(resourceLockEntity)) {
                return true;
            }
            Long endTime = System.currentTimeMillis();
            if (time < endTime - startTime) {
                return false;
            }
        }
    }


    public void lock() {
        lock(getResourceLockEntity());
    }

    private ResourceLockEntity getResourceLockEntity() {
        ResourceLockEntity lockEntity = new ResourceLockEntity();
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String name = Thread.currentThread().getName();
            String hostAddress = addr.getHostAddress();
            lockEntity.setResourceName(name + hostAddress);
            lockEntity.setNodeInfo(hostAddress);
        } catch (UnknownHostException e) {
            log.error(e.toString());
        }
        return lockEntity;
    }
}
