package com.fuli.server.base.util;

import com.fuli.server.base.lock.DistributedLock;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @Description:  redis 分布式锁工具类。提供常用加锁，解锁方法。
 * @Date: 2018/4/23 10:49
 * @Version: 1.0
 * @Author : YOUJIN
 */
public final class RedisLockUtil {
    private static DistributedLock redisLock;

    public static void setLocker(DistributedLock locker) {
        redisLock = locker;
    }

    /**
     * 加锁
     * @param lockKey
     * @return
     */
    public static RLock lock(String lockKey) {
        return redisLock.lock(lockKey);
    }

    /**
     * 释放锁
     * @param lockKey
     */
    public static void unlock(String lockKey) {
        redisLock.unlock(lockKey);
    }

    /**
     * 释放锁
     * @param lock
     */
    public static void unlock(RLock lock) {
        redisLock.unlock(lock);
    }

    /**
     * 带超时的锁
     * @param lockKey
     * @param timeout 超时时间   单位：秒
     */
    public static RLock lock(String lockKey, int timeout) {
        return redisLock.lock(lockKey, timeout);
    }

    /**
     * 带超时的锁
     * @param lockKey
     * @param unit 时间单位
     * @param timeout 超时时间
     */
    public static RLock lock(String lockKey, TimeUnit unit , int timeout) {
        return redisLock.lock(lockKey, unit, timeout);
    }

    /**
     * 尝试获取锁
     * @param lockKey
     * @param waitTime 最多等待时间  单位（s）
     * @param leaseTime 上锁后自动释放锁时间 单位（s）
     * @return
     */
    public static boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        return redisLock.tryLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
    }

    /**
     * 尝试获取锁
     * @param lockKey
     * @param unit 时间单位
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        return redisLock.tryLock(lockKey, unit, waitTime, leaseTime);
    }
}
