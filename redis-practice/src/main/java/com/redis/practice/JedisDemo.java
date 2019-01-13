package com.redis.practice;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Tuple;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project: redis
 * @description: jedis的api单机使用
 * @author: sunkang
 * @create: 2019-01-12 14:24
 * @ModificationHistory who      when       What
 **/
public class JedisDemo {

    public static void main(String[] args) {
        //三种方式连接
        //1.通过直接的host连接
        String host = "192.168.44.129";
        //超时时间为15000ms
        Jedis jedis = new Jedis(host,6379,15000);
        jedis.set("foo", "bar");
        String value = jedis.get("foo");
        System.out.println(value);

        //2.通过HostAndPort 连接
        HostAndPort hostAndPort = new HostAndPort(host,6379);
        Jedis jedis1 = new Jedis(hostAndPort);
        //对字符串的简单操作
        jedis1.set("bar","foo");
        String bar = jedis.get("bar");
        System.out.println(bar);
        //关闭
        jedis1.close();

        //3.通过url连接
        Jedis jedis2 = new Jedis("redis://"+host+":6379");
        System.out.println( jedis2.get("bar"));
        jedis2.close();

        //对list的简单操作
        jedis.lpush("list","value1","value2","value3");
        List<String> list = jedis.lrange("list",0,-1);
        System.out.println(list);


        //对map的简单操作
        Map<String,String> map  = new HashMap();
        map.put("key1","value1");
        map.put("key2","value2");
        jedis.hmset("map",map);
        Map getMap = jedis.hgetAll("map");
        System.out.println(getMap);

        //对set的结合的简单操作
        jedis.sadd("sets","set1","set2","set3");
         Set<String> sets= jedis.smembers("sets");
        System.out.println(sets);

        //对有序集合的检查操作
        jedis.zadd("zsets",1,"number1");
        jedis.zadd("zsets",2,"number2");
        Set<Tuple> zsets =  jedis.zrangeWithScores("zsets",0,-1);
        for(Tuple tuple : zsets){
            System.out.println(" scope: "+tuple.getScore()+ ",element:"+tuple.getElement());
        }
        jedis.close();
    }
}
