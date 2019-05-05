package com.hgy.distributed.lock.annotation;

import com.hgy.distributed.lock.DistributeLock;
import com.hgy.distributed.lock.redis.utils.RedisDistributedLock;

import java.lang.annotation.*;

/**
 * @author heguoya
 * @version 1.0
 * @className DistributedLock
 * @description 分布式锁注解
 * @date 2019/5/5 16:24
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DistributedLock {

    /**
     * 锁的key<br/>
     * 如果想增加坑的个数添加非固定锁，可以在参数上
     *
     */
    String synKey() default "";

    /**
     * 持锁时间，超时时间，持锁超过此时间自动丢弃锁<br/>
     * 单位毫秒,默认20秒<br/>
     * 如果为0表示永远不释放锁，在设置为0的情况下toWait为true是没有意义的<br/>
     * 但是没有比较强的业务要求下，不建议设置为0
     */
    long keepMills() default 20 * 1000;

    /**
     * 当获取锁失败，是继续等待还是放弃<br/>
     * 默认为继续等待
     */
    boolean toWait() default true;

    /**
     * 没有获取到锁的情况下且toWait()为继续等待，睡眠指定毫秒数继续获取锁，也就是轮训获取锁的时间<br/>
     * 默认为10毫秒
     *
     * @return
     */
    long sleepMills() default 10;

    /**
     * 锁获取超时时间：<br/>
     * 没有获取到锁的情况下且toWait()为true继续等待，最大等待时间，如果超时抛出
     * {@link java.util.concurrent.TimeoutException}
     * ，可捕获此异常做相应业务处理；<br/>
     * 单位毫秒,默认一分钟，如果设置为0即为没有超时时间，一直获取下去；
     *
     * @return
     */
    long maxSleepMills() default 60 * 1000;

    /**
     * // 锁等待重试次数，-1未不限制
     * @return
     */
    int retryCount() default -1;
    /**
     * 默认redis分布式锁
     * @return
     */
     Class<? extends DistributeLock> type() default RedisDistributedLock.class;
}
