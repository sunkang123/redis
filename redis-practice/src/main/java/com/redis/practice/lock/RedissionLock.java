package com.redis.practice.lock;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import sun.plugin2.message.BestJREAvailableMessage;

import java.util.concurrent.TimeUnit;

/**
 * @Project: redis
 * @description:  基于redission的api实现分布式锁
 * @author: sunkang
 * @create: 2019-01-12 18:08
 * @ModificationHistory who      when       What
 **/
public class RedissionLock {
    public static void main(String[] args) {
        Config config = new Config();
        //使用单个节点连接，并且设置连接地址
        config.useSingleServer().setAddress("redis://192.168.44.129:6379");
        RedissonClient redissonClient = Redisson.create(config);
        RLock lock =  redissonClient.getLock("updateOrder");
        try {
            //尝试获取锁100秒，锁释放时间为10秒
            lock.tryLock(100,10,TimeUnit.SECONDS);
            System.out.println("得到锁");
            Thread.sleep(100);
            //释放锁
            lock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
