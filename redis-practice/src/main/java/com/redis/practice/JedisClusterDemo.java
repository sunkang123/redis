package com.redis.practice;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @Project: redis
 * @description: redis集群模式下JedisCluster的api使用
 * 需要配置好redis的集群
 *连接的地址redis集群的地址
 * @author: sunkang
 * @create: 2019-01-12 14:52
 * @ModificationHistory who      when       What
 **/
public class JedisClusterDemo {
    public static void main(String[] args) {
        String clusterhos ="192.168.44.129";
        Set<HostAndPort> hostAndPorts = new HashSet<HostAndPort>();
        hostAndPorts.add(new HostAndPort(clusterhos,30001));
        hostAndPorts.add(new HostAndPort(clusterhos,30002));
        hostAndPorts.add(new HostAndPort(clusterhos,30003));
        hostAndPorts.add(new HostAndPort(clusterhos,30004));
        hostAndPorts.add(new HostAndPort(clusterhos,30005));
        hostAndPorts.add(new HostAndPort(clusterhos,30006));

        //在构建JedisCluster实例，其实会发送cluster slots指令来获取slot与节点的映射关系
        JedisCluster cluster = new JedisCluster(hostAndPorts);
        //设置一个值会利用crc16算法计算出slot的值，然后根据slot的值获取到与哪台节点的连接
        cluster.set("cluster","cluster");
        String value = cluster.get("cluster");
        System.out.println(value);
        cluster.close();
    }
}
