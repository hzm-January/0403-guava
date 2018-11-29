package houzm.accumulation.guava;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.util.concurrent.RateLimiter;

/**
 * Package: houzm.accumulation.guava
 * Author: houzm
 * Date: Created in 2018/10/21 22:26
 * Copyright: Copyright (c) 2018
 * Version: 0.0.1
 * Modified By:
 * Description： 平滑预热限流
 */
public class SmoothWarmingUp {
    private static Logger logger = LoggerFactory.getLogger(SmoothWarmingUp.class);

    public static void main(String[] args) {
        // permitsPerSecond 每秒新增令牌数
        // warmupPeriod 从冷启动速率过度到平均速率的时间间隔
//        RateLimiter rateLimiter = RateLimiter.create(5, 1, TimeUnit.SECONDS);
//        IntStream.rangeClosed(1, 10).forEach(key->{
//            logger.debug("key : {} , get acquire time : {} , current time : {}", key, rateLimiter.acquire(), System.currentTimeMillis());
//        });

        //测试无过渡时间
//        RateLimiter rateLimiter = RateLimiter.create(5, 0, TimeUnit.SECONDS);
//        IntStream.rangeClosed(1, 10).forEach(key->{
//            logger.debug("key : {} , get acquire time : {} , current time : {}", key, rateLimiter.acquire(), System.currentTimeMillis());
//        });

        //测试突发并发
        RateLimiter rateLimiter = RateLimiter.create(5, 1, TimeUnit.SECONDS);
        IntStream.rangeClosed(1, 5).forEach(key->{
            logger.debug("key : {} , get acquire time : {} , current time : {}", key, rateLimiter.acquire(), System.currentTimeMillis());
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        IntStream.rangeClosed(1, 10).forEach(key->{
            logger.debug("key : {} , get acquire time : {} , current time : {}", key, rateLimiter.acquire(), System.currentTimeMillis());
        });
    }
}
