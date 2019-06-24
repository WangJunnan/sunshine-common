package com.walm.common.redis;

import com.alibaba.fastjson.JSON;
import com.walm.common.util.NumberUtils;
import com.walm.common.util.PropertiesLoaderUtils;
import com.walm.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
            log.error("error_RedisOps_loadAllProperties", e);
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

    @FunctionalInterface
    public interface RedisExecutor<T> {
        T execute(Jedis jedis);
    }

    public <T> T execute(RedisExecutor<T> executor) {
        Jedis jedis = getJedis();
        T result = null;
        try {
            result = executor.execute(jedis);
        } catch (Exception e) {
            log.error("error_execute_command", e);
        } finally {
            jedis.close();
        }
        return result;
    }

    /**
     * get
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return execute(jedis -> jedis.get(key));
    }

    /**
     * 获取 object
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getObject(String key, Class<T> clazz) {
        String value = get(key);
        return JSON.parseObject(value, clazz);
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        return execute(jedis -> jedis.set(key, value));
    }

    /**
     * 存储 object
     *
     * @param key
     * @param t
     * @return
     */
    public String setObject(String key, T t) {
        String value = JSON.toJSONString(t);
        return set(key, value);
    }

    /**
     * setnx 成功设置返回1 失败返回0
     *
     * @param key
     * @param value
     * @return
     */
    public Long setnx(String key, String value) {
        return execute(jedis -> jedis.setnx(key, value));
    }

    /**
     * 自增
     *
     * @param key
     * @return
     */
    public Long incr(String key) {
        return execute(jedis -> jedis.incr(key));
    }

    /**
     * 自减
     *
     * @param key
     * @return
     */
    public Long decr(String key) {
        return execute(jedis -> jedis.decr(key));
    }

    /**
     * 增量increment 自增
     *
     * @param key
     * @param increment
     * @return
     */
    public Long incrBy(String key, Long increment) {
        return execute(jedis -> jedis.incrBy(key, increment));
    }

    /**
     * decrement 自减
     *
     * @param key
     * @param decrement
     * @return
     */
    public Long decrBy(String key, Long decrement) {
        return execute(jedis -> jedis.decrBy(key, decrement));
    }

    /**
     * 删除指定键值
     *
     * @param key
     * @return 成功返回 1 失败 0
     */
    public Long del(String key) {
        return execute(jedis -> jedis.del(key));
    }

    /**
     * 获取过期时间
     *
     * @param key
     * @return
     */
    public Long ttl(String key) {
        return execute(jedis -> jedis.ttl(key));
    }

    /**
     * 设置指定key超时时间  ttl
     *
     * @param key
     * @param ttl
     * @return 成功返回 1 失败 0
     */
    public Long expire(String key, /* second */int ttl) {
        return execute(jedis -> jedis.expire(key, ttl));
    }

    /**
     * hset
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hset(String key, final String field, final String value) {
        return execute(jedis -> jedis.hset(key, field, value));
    }

    /**
     * hget
     *
     * @param key
     * @param field
     * @return
     */
    public String hget(String key, final String field) {
        return execute(jedis -> jedis.hget(key, field));
    }

    /**
     * 删除指定 field
     *
     * @param key
     * @param field
     * @return
     */
    public Long hdel(String key, final String field) {
        return execute(jedis -> jedis.hdel(key, field));
    }

    /**
     * 判断指定 field 是否存在
     * @param key
     * @param field
     * @return
     */
    public Boolean hexists(String key, final String field) {
        return execute(jedis -> jedis.hexists(key, field));
    }

    /**
     * 获取全部
     *
     * @param key
     * @return
     */
    public Map<String, String> hexists(String key) {
        return execute(jedis -> jedis.hgetAll(key));
    }

    public String hmset(String key, Map<String, String> hash) {
        return execute(jedis -> jedis.hmset(key, hash));
    }

    public List<String> hmget(String key, final String... fields) {
        return execute(jedis -> jedis.hmget(key, fields));
    }
}
