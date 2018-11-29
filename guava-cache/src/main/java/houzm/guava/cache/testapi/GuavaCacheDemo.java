package houzm.guava.cache.testapi;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * Package: houzm.guava.cache.testapi
 * Author: houzm
 * Date: Created in 2018/10/13 12:29
 * Copyright: Copyright (c) 2018
 * Version: 0.0.1
 * Modified By:
 * Description： guava cache demo
 *
 * guava cache 类似于java中的map，并提供了一些map没有的强大的功能
 * 1. 创建实例：
 *          通过CacheBuilder创建，CacheBuilder采用builder设计模式，每个方法都返回CacheBuilder直到调用build()方法
 * 2. 设置最大存储：
 *          构建缓存对象时可以指定缓存最大存储记录数
 *          当Cache中的记录数达到最大值后，再次put添加对象，会从当期缓存的对象记录中选择一条删除，腾出空间后将新的对象存储到Cache中
 * 3. 设置过期时间：
 *          构建缓存对象时可以设置过期时间
 *          expireAfterAccess n秒未访问，过期自动删除
 *          expireAfterWrite  写入n秒后，过期自动删除
 * 4. 显式删除：
 *          删除指定键值
 * 5. 显式批量删除：
 *          删除批量指定键值 void invalidateAll(Iterable<?> keys)
 *          删除所有键值 void invalidateAll();
 * 6. 添加移除监听器
 *          构建缓存对象时，可设置监听器，用于监听键的删除
 * 7. 自动加载
 *          Cache的get方法有两个参数，第一个参数是要从Cache中获取记录的key，第二个参数是一个Callable对象。
 *          当缓存中已经存在key对应的记录时，get方法直接返回key对应的记录。
 *          如果缓存中不包含key对应的记录，Guava会启动一个线程执行Callable对象中的call方法，
 *          call方法的返回值会作为key对应的值被存储到缓存中，并且被get方法返回
 * 8. 统计信息
 * 9. LoadingCache
 */
public class GuavaCacheDemo {
    private static Logger logger = LoggerFactory.getLogger(GuavaCacheDemo.class);
    public static void main(String[] args) {
        //1. 创建缓存
        createCache();
        //2. 新建缓存对象时，指定缓存所能够存储的最大记录数
//        maximumSize();
        //3.1 设置过期时间----n秒没访问自动过期删除
//        expireAfterAccess();
        //3.2 设置过期时间----写入n秒之后自动过期删除
//        expireAfterWrite();
        //4. 显示清除
//        invalidate();
        //5. 显示批量清除
//        invalidateAll();
        //6. 添加移除监听器
//        removaListener();
        //7. 自动加载
//        autoLoadOfGet();
        //8. 统计信息
//        recordStats();
        //9. LoadingCache
//        loadingCache();
    }

    private static void loadingCache() {
        LoadingCache<Long, String> cache = CacheBuilder.newBuilder().build(new CacheLoader<Long, String>() {
            @Override
            public String load(Long key) throws Exception {
                logger.debug(" the key {} is non-existent in cache which the value {} will be set", key, key * 2);
                return String.valueOf(key * 2);
            }
        });
        cache.put(10000000001L, "20000000002");
        try {
            cache.get(10000000002L);
            cache.get(10000000003L);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 统计信息
     */
    private static void recordStats() {
        Cache<Long, String> cache = CacheBuilder.newBuilder()
                .recordStats()
                .build();
        cache.put(10000000001L, "hello-world!--1");
        cache.put(10000000002L, "hello-world!--2");
        cache.put(10000000003L, "hello-world!--3");
        cache.put(10000000004L, "hello-world!--4");
        cache.put(10000000005L, "hello-world!--5");
        cache.getIfPresent(10000000001L);
        cache.getIfPresent(10000000002L);
        cache.getIfPresent(10000000003L);
        cache.getIfPresent(10000000004L);
        cache.getIfPresent(10000000005L);
        logger.debug("{}", cache.stats());
    }

    private static void autoLoadOfGet() {
        Cache<Long, String> cache = CacheBuilder.newBuilder().build();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch countDownLatch = new CountDownLatch(2);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    cache.get(10000000001L, new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                            logger.debug("10000000001L not exist , but key-value will be set in cache {}", "hello worlds!");
                            Thread.sleep(1000);
                            return "hello worlds!";
                        }
                    });
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    cache.get(10000000001L, new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                            logger.debug("10000000001L not exist , but key-value will be set in cache {}", "hello worlds--2!");
                            Thread.sleep(1000);
                            return "hello worlds--2!";
                        }
                    });
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            }
        });
        executor.shutdown();
        try {
            countDownLatch.await(1000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        @Nullable String cacheKey = cache.getIfPresent(10000000001L);
        logger.debug("10000000001L---{}", cacheKey);
    }

    /**
     * 添加移除监听器
     */
    private static void removaListener() {
        RemovalListener<Long, String> listener = new RemovalListener<Long, String>() {
            @Override
            public void onRemoval(RemovalNotification<Long, String> notification) {
                logger.debug("[{} : {}] is removed! because {}", notification.getKey(), notification.getValue(), notification.getCause());
            }
        };
        Cache<Long, String> cache = CacheBuilder.newBuilder()
                .maximumSize(3) //设置最大存储
                .removalListener(listener)
                .build();
        cache.put(10000000001L, "hello world!--1");
        cache.put(10000000002L, "hello world!--2");
        cache.put(10000000003L, "hello world!--3");
        List<Long> cacheKeys = new LinkedList<>(Arrays.asList(new Long[]{10000000001L, 10000000002L}));
        logger.debug("before invalidate 10000000001L ：{}", cache.getIfPresent(10000000001L));
        logger.debug("before invalidate 10000000002L ：{}", cache.getIfPresent(10000000002L));
        logger.debug("before invalidate 10000000003L ：{}", cache.getIfPresent(10000000003L));
        cache.invalidateAll(cacheKeys);
//        cache.invalidateAll(); //清除所有key
        logger.debug("after invalidate 10000000001L ：{}", cache.getIfPresent(10000000001L));
        logger.debug("after invalidate 10000000002L ：{}", cache.getIfPresent(10000000002L));
        logger.debug("after invalidate 10000000003L ：{}", cache.getIfPresent(10000000003L));
    }

    /**
     * 显示批量清除
     */
    private static void invalidateAll() {
        Cache<Long, String> cache = CacheBuilder.newBuilder()
                .maximumSize(3) //设置最大存储
                .build();
        cache.put(10000000001L, "hello world!--1");
        cache.put(10000000002L, "hello world!--2");
        cache.put(10000000003L, "hello world!--3");
        List<Long> cacheKeys = new LinkedList<>(Arrays.asList(new Long[]{10000000001L, 10000000002L}));
        logger.debug("before invalidate 10000000001L ：{}", cache.getIfPresent(10000000001L));
        logger.debug("before invalidate 10000000002L ：{}", cache.getIfPresent(10000000002L));
        logger.debug("before invalidate 10000000003L ：{}", cache.getIfPresent(10000000003L));
        cache.invalidateAll(cacheKeys);
//        cache.invalidateAll(); //清除所有key
        logger.debug("after invalidate 10000000001L ：{}", cache.getIfPresent(10000000001L));
        logger.debug("after invalidate 10000000002L ：{}", cache.getIfPresent(10000000002L));
        logger.debug("after invalidate 10000000003L ：{}", cache.getIfPresent(10000000003L));
    }

    /**
     * 显示清除
     */
    private static void invalidate() {
        Cache<Long, String> cache = CacheBuilder.newBuilder()
                .maximumSize(3) //设置最大存储
                .build();
        cache.put(10000000001L, "hello world!--1");
        logger.debug("before invalidate 10000000001L ：{}", cache.getIfPresent(10000000001L));
        cache.invalidate(10000000001L);
        logger.debug("after invalidate 10000000001L ：{}", cache.getIfPresent(10000000001L));
    }

    /**
     * 设置过期时间----写入n秒之后自动过期删除
     */
    private static void expireAfterWrite() {
        Cache<Long, String> cache = CacheBuilder.newBuilder()
                .maximumSize(3) //设置最大存储
                .expireAfterWrite(2, TimeUnit.SECONDS) //写入缓存2s后就过期自动删除
                .build();
        cache.put(10000000001L, "hello world!--1");
        AtomicInteger time = new AtomicInteger();
        IntStream.rangeClosed(1, 3).forEach(key->{
            @Nullable String value = cache.getIfPresent(10000000001L); //null
            logger.debug(value);
            try {
                Thread.sleep(1000); //休眠1s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 设置过期时间----n秒没访问自动过期删除
     */
    private static void expireAfterAccess() {
        Cache<Long, String> cache = CacheBuilder.newBuilder()
                .maximumSize(3) //设置最大存储
                .expireAfterAccess(2, TimeUnit.SECONDS) //2s没被访问就过期自动删除
                .build();
        cache.put(10000000001L, "hello world!--1");
        AtomicInteger time = new AtomicInteger();
        IntStream.rangeClosed(1, 3).forEach(key->{
            @Nullable String value = cache.getIfPresent(10000000001L); //null
            logger.debug(value);
            try {
                if (key == 1) {
                    Thread.sleep(1000); //休眠1s，再次访问
                } else {
                    Thread.sleep(2500); //休眠2.5s，让值过期
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 新建缓存对象时，指定缓存所能够存储的最大记录数
     */
    private static void maximumSize() {
        Cache<Long, String> cache = CacheBuilder.newBuilder()
                .maximumSize(2) //设置最大存储
                .build();
        cache.put(10000000001L, "hello world!--1");
        cache.put(10000000002L, "hello world!--2");
        cache.put(10000000003L, "hello world!--3");
        @Nullable String value = cache.getIfPresent(10000000001L); //null
        @Nullable String value2 = cache.getIfPresent(10000000002L); //hello world!--2
        @Nullable String value3 = cache.getIfPresent(10000000003L); //hello world!--3
        logger.debug(value);
        logger.debug(value2);
        logger.debug(value3);
    }

    /**
     * 创建缓存
     */
    private static void createCache() {
        //1. 创建一个key类型为Long，value类型为String的缓存对象
        Cache<Long, String> cache = CacheBuilder.newBuilder().build();
        cache.put(10000000001L, "hello world!");
        @Nullable String value = cache.getIfPresent(10000000001L);
        logger.debug(value);
    }
}
