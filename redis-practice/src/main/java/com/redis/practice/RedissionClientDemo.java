package com.redis.practice;

import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @Project: redis
 * @description:
 * @author: sunkang
 * @create: 2019-01-12 15:35
 * @ModificationHistory who      when       What
 **/
public class RedissionClientDemo {


    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.44.129:6379");

        RedissonClient redissonClient  = Redisson.create(config);

//        redissonClient.getLock("");//分布式锁

        redissonClient.getBucket("string").set("string");

        redissonClient.getList("list").add("value1");




    }
}
