package com.redis.practice.lock;

import com.redis.practice.JedisPoolUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import javax.security.auth.callback.CallbackHandler;
import java.util.UUID;

/**
 * @Project: redis
 * @description:   redis的分布式锁的手动实现
 * @author: sunkang
 * @create: 2019-01-12 16:54
 * @ModificationHistory who      when       What
 **/
public class DistributedLock {

    /**
     * 获取锁
     * @param lockName  锁的名称
     * @param accquireTimeout   获取锁的超时时间
     * @param lockTimeout  锁本身过期时间
     * @return
     */
    public String acquireLock(String lockName ,long accquireTimeout,long  lockTimeout){
        String identify  = UUID.randomUUID().toString();
        String lockKey = "lock:"+lockName;
        //获取redis客户端
        long endTime = System.currentTimeMillis()+ accquireTimeout;
        Jedis redis = JedisPoolUtils.getRedis();
        int expireTime = (int) (lockTimeout/1000);

        try{
            while (System.currentTimeMillis()<endTime){
                //setnx表示如果键不存在则设置，返回1证明key设置成功了
                if( redis.setnx(lockKey,identify) ==1 ){
                    redis.expire(lockKey, expireTime);
                    return identify;
                }
                //说明key的过期时间没有设置，但是key存在,设置过期时间
                if(redis.ttl(lockKey) == -1){
                    redis.expire(lockKey,expireTime);
                }
                try {
                    //等待片刻后进行获取锁的重试
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }finally {
            //最终关闭连接
            redis.close();
        }
        return null;
    }
    /**
     * 释放锁
     * @param lockName
     * @param identifer
     */
    public boolean releaseLock(String lockName,String identifer){
        System.out.println(lockName+"开始释放锁："+identifer);
        boolean release = false;
        String lockKey  = "lock:"+ lockName;
        Jedis jedis  = JedisPoolUtils.getRedis();
        try {
            while (true){
                //命令用于监视一个(或多个) key ，如果在事务执行之前这个key,被其他命令所改动，那么事务将被打断
                //保证了开启监控到执行对该key执行事务提交之间，不能修改，保证了这段期间key的数据原子性。
                jedis.watch(lockKey);
                //如果是同一把锁
                if(jedis.get(lockKey).equals(identifer)){
                    Transaction transaction  = jedis.multi();
                    //主动删除锁
                    transaction.del(lockKey);
                    //执行结果为空，说明执行有问题，在轮训一次
                    if(transaction.exec().isEmpty()){
                        release = false;
                        continue;
                    }
                    release =true;
                }
                jedis.unwatch();
                break;
            }
        }finally {
            jedis.close();
        }
        return release;
    }

    /**
     * 通过lua脚本来释放锁
     * lua脚本执行保证了原子性
     * @param lockName
     * @param identifer
     * @return
     */
    public boolean  releaseLockWithLua(String lockName,String identifer){

        System.out.println(lockName+"开始释放锁："+identifer);

        Jedis jedis  =     JedisPoolUtils.getRedis();
        String lockKey = "lock:"+ lockName;

        String lua ="if redis.call('get',KEYS[1]) == ARGV[1] then " +
                "return redis.call('del',KEYS[1])" +
                "else return 0 end ";
        Long rs=(Long)jedis.eval(lua,1,lockKey,identifer);

        jedis.close();

        if(rs.intValue()> 0){
            return true;
        }
        return  false;
    }



}
