package com.hgy.distributed.lock.redis.utils;

import com.hgy.distributed.lock.DistributeLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.locks.LockSupport;

/**
 * @author heguoya
 * @version 1.0
 * @className RedisDistributedLock
 * @description redis分布式锁
 * @date 2019/5/5 17:01
 */
@Slf4j
public class RedisDistributedLock implements DistributeLock {

    private final StringRedisTemplate redisTemplate;
    private final String lockKey;

    private final String lockValue;

    private boolean locked = false;

    /**
     * 使用脚本在redis服务器执行这个逻辑可以在一定程度上保证此操作的原子性
     * （即不会发生客户端在执行setNX和expire命令之间，发生崩溃或失去与服务器的连接导致expire没有得到执行，发生永久死锁）
     * <p>
     * 除非脚本在redis服务器执行时redis服务器发生崩溃，不过此种情况锁也会失效
     */
    private static final RedisScript<Boolean> SETNX_AND_EXPIRE_SCRIPT;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if (redis.call('setnx', KEYS[1], ARGV[1]) == 1) then\n");
        sb.append("\tredis.call('expire', KEYS[1], tonumber(ARGV[2]))\n");
        sb.append("\treturn true\n");
        sb.append("else\n");
        sb.append("\treturn false\n");
        sb.append("end");
        SETNX_AND_EXPIRE_SCRIPT = new RedisScriptImpl<>(sb.toString(), Boolean.class);
    }

    private static final RedisScript<Boolean> DEL_IF_GET_EQUALS;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if (redis.call('get', KEYS[1]) == ARGV[1]) then\n");
        sb.append("\tredis.call('del', KEYS[1])\n");
        sb.append("\treturn true\n");
        sb.append("else\n");
        sb.append("\treturn false\n");
        sb.append("end");
        DEL_IF_GET_EQUALS = new RedisScriptImpl<>(sb.toString(), Boolean.class);
    }

    public RedisDistributedLock(StringRedisTemplate redisTemplate, String lockKey) {
        this.redisTemplate = redisTemplate;
        this.lockKey = lockKey;
        this.lockValue = UUID.randomUUID().toString() + "." + System.currentTimeMillis();
    }

    /**
     * 加锁
     *
     * @return
     */
    @Override
    public boolean lock() {
        while (true) {
            if (redisTemplate.execute(SETNX_AND_EXPIRE_SCRIPT, Collections.singletonList(lockKey), lockValue)) {
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
        return redisTemplate.execute(SETNX_AND_EXPIRE_SCRIPT, Collections.singletonList(lockKey), lockValue);
    }

    /**
     * 尝试获取锁
     *
     * @param key
     * @return
     */
    @Override
    public boolean tryLock(String key) {
        return redisTemplate.execute(SETNX_AND_EXPIRE_SCRIPT, Collections.singletonList(key), lockValue);
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
        for (int i = 0; i < retry; i++) {
            if (tryLock(key)) {
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
        if (!locked) {
            throw new IllegalStateException("not locked yet!");
        }
        locked = false;
        return redisTemplate.execute(DEL_IF_GET_EQUALS, Collections.singletonList(lockKey), lockValue);
    }

    /**
     * 根据key释放锁
     *
     * @param key
     * @return
     */
    @Override
    public boolean unLock(String key) {
        if (!locked) {
            throw new IllegalStateException("not locked yet!");
        }
        locked = false;
        return redisTemplate.execute(DEL_IF_GET_EQUALS, Collections.singletonList(key), lockValue);
    }


    private static class RedisScriptImpl<T> implements RedisScript<T> {

        private final String script;

        private final String sha1;

        private final Class<T> resultType;

        public RedisScriptImpl(String script, Class<T> resultType) {
            this.script = script;
            this.sha1 = DigestUtils.sha1DigestAsHex(script);
            this.resultType = resultType;
        }

        @Override
        public String getSha1() {
            return sha1;
        }

        @Override
        public Class<T> getResultType() {
            return resultType;
        }

        @Override
        public String getScriptAsString() {
            return script;
        }
    }
}
