package com.redis.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.hash.HashMapper;

import java.util.List;
import java.util.Map;

/**
 * @Project: redis
 * @description: StringRedisTemplate 的简单使用
 * @author: sunkang
 * @create: 2019-01-12 21:12
 * @ModificationHistory who      when       What
 **/
public class RedisTemplateExample {
    public static void main(String[] args) {
        ApplicationContext context =  new ClassPathXmlApplicationContext("classpath:applicationContext-redis.xml");
        RedisTemplate  redisTemplate =  context.getBean("redisTemplate",StringRedisTemplate.class);
        //对普通字符串操  ,下面的两个方式相同
        //方式一： Key Type Operations
        redisTemplate.opsForValue().set("string","key");
        //方式二：Key Bound Operations
        redisTemplate.boundValueOps("string").set("key1");

        //对list进行操作
        //方式一： Key Type Operations
        redisTemplate.opsForList().leftPush("listKey","element");
        //方式二：Key Bound Operations
        redisTemplate.boundListOps("listKey").rightPush("element2");

        List<String> list =   redisTemplate.opsForList().range("listKey",0,-1);
        System.out.println(list);

        //其他的数据接口方式不再演示
        //hash
        redisTemplate.opsForHash().put("hash","key1","values");
         //set
        redisTemplate.opsForSet().add("set","set1","set2");
        //HyperLogLog
        redisTemplate.opsForHyperLogLog().add("hyper","123");
        //zset
        redisTemplate.opsForZSet().add("zset","number1",1);


        //使用RedisCallback 接口来操作数据
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Long size = redisConnection.dbSize();
                //直接使用StringRedisConnection连接实例来操作数据
                StringRedisConnection connection =  (StringRedisConnection) redisConnection;
                connection.set("key","value");
                return null;
            }
        });
        //HashMapping  对象和map互相转换
        Person person = new Person();
        person.setFirstname("sun");
        person.setLastname("kang");
        //写进redis
        writeHash("person",person,redisTemplate);
        //从reids获取数据
        Person person1 =   loadHash("person",redisTemplate);
        System.out.println(person1);

        //使用pipeline
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisConnection stringRedisConn = (StringRedisConnection)connection;
                for(int i=0; i< 10; i++) {
                    stringRedisConn.lPush("myqueue",i+"");
                }
                return null;
            }
        });

        //其他的方式可以参考官网，这里不再一一接收


    }

    public static void writeHash(String key, Person person,RedisTemplate redisTemplate) {
        HashMapper<Person, String, String > mapper = new PersonHashMapper();
        //把person 转换成mapper
        Map<String,String> map =  mapper.toHash(person);
        redisTemplate.opsForHash().putAll(key,map);
    }

    public static Person loadHash(String key,RedisTemplate redisTemplate) {
        Map<String, String> loadedHash =  redisTemplate.opsForHash().entries(key);
        HashMapper<Person, String, String > mapper = new PersonHashMapper();
        //把map 转成person实体
        Person person =   mapper.fromHash(loadedHash);
        return person;
    }

}
