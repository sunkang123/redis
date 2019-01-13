package com.redis.practice.lock;

/**
 * @Project: redis
 * @description: 分布式测试
 * 开启10个线程，模拟十个客户端请求锁
 * @author: sunkang
 * @create: 2019-01-12 17:45
 * @ModificationHistory who      when       What
 **/
public class LockTest extends Thread{
    @Override
    public void run() {
        while(true){
            DistributedLock distributedLock=new DistributedLock();
            String rs=distributedLock.acquireLock("updateOrder",
                    2000,5000);
            if(rs!=null){
                System.out.println(Thread.currentThread().getName()+"-> 成功获得锁:"+rs);
                try {
                    Thread.sleep(1000);
                    //释放锁
                    distributedLock.releaseLockWithLua("updateOrder",rs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    public static void main(String[] args) {
        LockTest lockTest=new LockTest();
        for(int i=0;i<10;i++){
            new Thread(lockTest,"tName:"+i).start();
        }
    }
}
