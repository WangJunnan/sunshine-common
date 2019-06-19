package com.walm.common.redis;

import com.walm.common.util.NumberUtils;
import com.walm.common.util.PropertiesLoaderUtils;
import com.walm.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Properties;

/**
 * <p>RedisOps</p>
 *
 * @author wangjn
 * @date 2019/6/5
 */
@Slf4j
public class RedisOps {

    public static final RedisOps INSTANCE = new RedisOps();

    private JedisPool jedisPool;


    private RedisOps() {
        init();
    }

    private void init() {
        Properties properties = null;
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("redis.properties");
        } catch (IOException e) {
        }
        // 操作超时时间,默认2秒
        int timeout = NumberUtils.toInt(properties.getProperty("redis.timeout"), 2000);
        // jedis池最大连接数总数，默认8
        int maxTotal = NumberUtils.toInt(properties.getProperty("redis.maxTotal"), 8);
        // jedis池最大空闲连接数，默认8
        int maxIdle = NumberUtils.toInt(properties.getProperty("redis.maxIdle"), 8);
        // jedis池最少空闲连接数
        int minIdle = NumberUtils.toInt(properties.getProperty("redis.minIdle"), 0);
        // jedis池没有对象返回时，最大等待时间单位为毫秒
        long maxWaitMillis = NumberUtils.toLong(properties.getProperty("redis.maxWaitTime"), -1L);
        // 在borrow一个jedis实例时，是否提前进行validate操作
        boolean testOnBorrow = Boolean.parseBoolean(properties.getProperty("redis.testOnBorrow"));

        // 设置jedis连接池配置
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setTestOnBorrow(testOnBorrow);
        // 取得redis的url
        String redisHost = properties.getProperty("redis.host");
        Integer redisPort = NumberUtils.toInt(properties.getProperty("redis.port"), 6379);
        if (StringUtils.isEmpty(redisHost)) {
            throw new IllegalStateException("the host of redis is not configured");
        }

        jedisPool = new JedisPool(poolConfig, redisHost, redisPort, timeout);
    }

    private Jedis getJedis() {
        return jedisPool.getResource();
    }

    public String get(String key) {
        return getJedis().get(key);
    }

    public String set(String key, String value) {
        return getJedis().set(key, value);
    }
}
