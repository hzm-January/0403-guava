package houzm.accumulation.guava;

import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.util.concurrent.RateLimiter;

/**
 * Package: houzm.accumulation.guava
 * Author: houzm
 * Date: Created in 2018/10/21 21:54
 * Copyright: Copyright (c) 2018
 * Version: 0.0.1
 * Modified By:
 * Description： SmoothBursty
 */
public class SmoothBursty {
    private static Logger logger = LoggerFactory.getLogger(SmoothBursty.class);
    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(5); //桶容量5，每秒5个令牌，每隔200ms一个令牌
        //测试平均固定频率
//        IntStream.rangeClosed(1, 10).forEach(key->{
//            logger.debug("{}", rateLimiter.acquire());
//        });
        //测试突发固定频率--1
//        logger.debug("{}", rateLimiter.acquire(5)); //0.0
//        logger.debug("{}", rateLimiter.acquire()); //0.992862
//        logger.debug("{}", rateLimiter.acquire()); //0.197419
//        logger.debug("{}", rateLimiter.acquire()); //0.199719

        //测试突发固定频率--2
//        logger.debug("{}", rateLimiter.acquire(10)); //0.0
//        logger.debug("{}", rateLimiter.acquire()); //1.993489
//        logger.debug("{}", rateLimiter.acquire()); //0.197795
//        logger.debug("{}", rateLimiter.acquire()); //0.19956

        //测试突发固定频率--3
        logger.debug("{}", rateLimiter.acquire()); //0.0
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        IntStream.rangeClosed(1, 10).forEach(key->{
            logger.debug("key : {} , get acquire time ： {} , current time : {}", key, rateLimiter.acquire(), System.currentTimeMillis()); //1.993489
        });
    }
}
