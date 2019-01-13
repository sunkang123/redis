package com.redis.practice;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * @Project: redis
 * @description:JedisSentinelPool 哨兵模式下的api使用
 * 配置的是哨兵的地址集群地址
 * 需要开启哨兵
 * @author: sunkang
 * @create: 2019-01-12 14:43
 * @ModificationHistory who      when       What
 **/
public class JedisSentinePoolDemo {

    public static void main(String[] args) {
        //配置的是哨兵的ip地址
        String host = "192.168.44.129:26379";
        String sentinelMasterName = "mymaster";
        Set<String> hostSet= new HashSet<String>();
        hostSet.add(host);
        //哨兵集群下的连接 ，通过连接哨兵集群从而获取得到jedisd 连接
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        JedisSentinelPool sentinelPool  = new JedisSentinelPool(sentinelMasterName,hostSet,config,20000);
        Jedis jedis  = sentinelPool.getResource();
        jedis.set("sentinel","sentinel");
        String value = jedis.get("sentinel");
        System.out.println(value);
    }
}
