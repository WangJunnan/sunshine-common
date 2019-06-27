package com.walm.common.util;

import com.walm.common.redis.RedisOps;

import java.util.Objects;

/**
 * <p>RedisSeqUtils</p>
 *
 * @author wangjn
 * @date 2019/6/27
 */
public class RedisSeqUtils {

    /**
     * 会员号生成的时间戳位数 25位 支持63年
     */
    private static final long TIMEOFMINUTE_BITS = 25;

    /**
     * 会员自增序列号位数
     */
    private static final long SEQUENCE_BITS = 19;
    /**
     * 会员类型 位数
     */
    private static final long SOURCE_BITS = 4;
    /**
     * 起始分钟级时间戳
     */
    private static final long START_MINUTE = 25445760L;

    private static final String MEMBERNO_INCR_KEY = "seq:incr:no:";

    public static long nextNo(long source) {
        // 获取精确到分的时间戳
        long nowTimeMinute = System.currentTimeMillis() / 1000 / 60;
        long timeMinute = nowTimeMinute - START_MINUTE;
        String key = MEMBERNO_INCR_KEY +  timeMinute;
        Long value = NumberUtils.parseNumber(RedisOps.INSTANCE.get(key), Long.class);
        if (Objects.isNull(value) || value == 0) {
            value = RedisOps.INSTANCE.incr(key);
            RedisOps.INSTANCE.expire(key, 90);
        } else {
            value = RedisOps.INSTANCE.incr(key);
        }

        if (value > (-1L ^ (-1L << SEQUENCE_BITS))) {
            // 基本不会出现这个异常
            throw new RuntimeException("序列已增长至最大值");
        }

        return timeMinute << (SEQUENCE_BITS + SOURCE_BITS) | value << SOURCE_BITS | source;
    }
}
