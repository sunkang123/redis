package com.redis.practice;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.awt.event.PaintEvent;

/**
 * @Project: redis
 * @description:  Pipeline的api简单使用
 * @author: sunkang
 * @create: 2019-01-12 18:28
 * @ModificationHistory who      when       What
 **/
public class PipelineDemo {
    public static void main(String[] args) {
        Jedis   jedis  = JedisPoolUtils.getRedis();
         Pipeline pipeline =  jedis.pipelined();
         //1.没有事务的同步
         pipeline.set("aa","1");
         pipeline.set("bb","2");
         pipeline.set("cc","3");
         pipeline.set("dd","4");
         pipeline.sync();

         //2.开启事务的pipeline
        try{
            pipeline.multi();
            pipeline.set("ee","ee");
            pipeline.set("ff","ff");
            //提交
            pipeline.exec();
        }catch (Exception e){
            //回滚
            pipeline.discard();
        }
        jedis.close();
    }
}
