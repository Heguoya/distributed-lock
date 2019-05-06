package com.hgy.distributed.lock.zookeeper;

import com.google.common.base.Strings;
import com.hgy.distributed.lock.DistributeLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * @author heguoya
 * @version 1.0
 * @className ZkDistributedLock
 * @description zk实现分布式锁
 * @date 2019/5/5 17:18
 */
@Slf4j
public class ZkDistributedLock implements DistributeLock {

    private String zkQurom = "localhost:2181";

    private String lockNameSpace = "/mylock";

    private String nodeString = lockNameSpace + "/test1";
    private ZooKeeper zk;

    public ZkDistributedLock() {
        try {
            zk = new ZooKeeper(zkQurom, 6000, watchedEvent -> {
                log.info("Receive event " + watchedEvent);
                if (Watcher.Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                    log.info("connection is established...");
                }
            });
        } catch (IOException e) {
            log.error(e.toString());
        }
    }

    private void ensureRootPath() {
        try {
            if (zk.exists(lockNameSpace, true) == null) {
                zk.create(lockNameSpace, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException | InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error(e.toString());
        }
    }

    private void watchNode(String nodeString, final Thread thread) {
        try {
            zk.exists(nodeString, watchedEvent -> {
                log.info("==" + watchedEvent.toString());
                if (watchedEvent.getType() == Watcher.Event.EventType.NodeDeleted) {
                    log.info("Threre is a Thread released Lock==============");
                    thread.interrupt();
                }
                try {
                    zk.exists(nodeString, watchedEvent1 -> {
                        log.info("==" + watchedEvent1.toString());
                        if (watchedEvent1.getType() == Watcher.Event.EventType.NodeDeleted) {
                            log.info("Threre is a Thread released Lock==============");
                            thread.interrupt();
                        }
                        try {
                            zk.exists(nodeString, true);
                        } catch (KeeperException | InterruptedException e) {
                            Thread.currentThread().interrupt();
                            log.error(e.toString());
                        }
                    });
                } catch (KeeperException | InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error(e.toString());
                }
            });
        } catch (KeeperException|InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error(e.toString());
        }
    }

    /**
     * 加锁
     *
     * @return
     */
    @Override
    public boolean lock() {
        ensureRootPath();
        watchNode(nodeString, Thread.currentThread());
        while (true) {
            if (getLock(nodeString)) {
                return true;
            }
        }
    }

    /**
     * 尝试获取锁
     *
     * @return
     */
    @Override
    public boolean tryLock() {
        return tryLock(nodeString);
    }

    /**
     * 尝试获取锁
     *
     * @param key
     * @return
     */
    @Override
    public boolean tryLock(String key) {
        ensureRootPath();
        watchNode(key, Thread.currentThread());
        return getLock(key);
    }

    private boolean getLock(String key) {
        String path = null;
        try {
            path = zk.create(key, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException | InterruptedException e) {
            log.info(Thread.currentThread().getName() + "  getting Lock but can not get");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                log.info("thread is notify");
            }
        }
        if (!Strings.nullToEmpty(path).trim().isEmpty()) {
            log.info(Thread.currentThread().getName() + "  get Lock...");
            return true;
        }
        return false;
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
        ensureRootPath();
        watchNode(key, Thread.currentThread());
        for (int i = 0; i < retry; i++) {
            if (getLock(key)) {
                return true;
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
        return unLock(nodeString);
    }

    /**
     * 根据key释放锁
     *
     * @param key
     * @return
     */
    @Override
    public boolean unLock(String key) {
        try {
            zk.delete(key, -1);
            log.info("Thread.currentThread().getName() +  release Lock...");
        } catch (InterruptedException | KeeperException e) {
            Thread.currentThread().interrupt();
            log.error(e.toString());
        }
        return true;
    }
}
