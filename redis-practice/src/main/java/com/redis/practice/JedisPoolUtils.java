package com.redis.practice;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Project: redis
 * @description: JedisPool的连接池api简单使用
 * @author: sunkang
 * @create: 2019-01-12 16:20
 * @ModificationHistory who      when       What
 **/
public class JedisPoolUtils {
   private static String host = "192.168.44.129";
   private static   JedisPool jedisPool = null;
    public static Jedis getRedis(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //设置jedis连接池中最大的连接个数
        poolConfig.setMaxTotal(60);
        //设置jedis连接池中最大的空闲连接个数
        poolConfig.setMaxIdle(30);
        //设置jedis连接池中最小的空闲连接个数
        poolConfig.setMinIdle(5);
        //jedis连接池最大的等待连接时间 ms值
        poolConfig.setMaxWaitMillis(30000);
         jedisPool = new JedisPool(poolConfig,host,6379,2000);
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }
    public void closePool(){
        if(jedisPool !=null){
            jedisPool.close();
        }
    }
}
