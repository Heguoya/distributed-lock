package com.hgy.distributed.lock;

import com.hgy.distributed.lock.mysql.service.ResourceLockService;
import com.hgy.distributed.lock.mysql.utils.MysqlDistributedLock;
import com.hgy.distributed.lock.redis.utils.RedisDistributedLock;
import com.hgy.distributed.lock.zookeeper.ZkDistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DistributedLockApplicationTests {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ResourceLockService resourceLockService;

    @Test
    public void mysql() {
        DistributeLock distributeLock = new MysqlDistributedLock(resourceLockService);
        distributeLock.lock();
        distributeLock.lock();
        await().atMost(5, TimeUnit.SECONDS);
        distributeLock.unLock();
    }
    @Test
    public void redis() {
        String key = "redisLock";
        DistributeLock distributeLock = new RedisDistributedLock(redisTemplate, key);
        distributeLock.lock();
        await().atMost(5, TimeUnit.SECONDS);
        distributeLock.unLock();
    }


    @Test
    public void zookeeper() {
        DistributeLock distributeLock = new ZkDistributedLock();
        distributeLock.lock();
        await().atMost(5, TimeUnit.SECONDS);
        distributeLock.unLock();
    }

}
