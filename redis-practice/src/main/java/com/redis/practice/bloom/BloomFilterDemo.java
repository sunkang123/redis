package com.redis.practice.bloom;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;

/**
 * @Project: redis
 * @description: 布隆过滤器
 * @author: sunkang
 * @create: 2019-01-12 19:22
 * @ModificationHistory who      when       What
 **/
public class BloomFilterDemo {
    public static void main(String[] args) {
        //插入的key为字符串，预计数据量为一百万，并且容错率为0.01
        BloomFilter bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()),
                1000000,0.01);
        bloomFilter.put("sunkang");
        System.out.println(bloomFilter.mightContain("sunkang"));
    }
}
