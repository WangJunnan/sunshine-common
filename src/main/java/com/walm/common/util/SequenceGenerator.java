package com.walm.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>SequenceGenerator</p>
 * <p>snowFlake id生成，支持一台机器每毫秒生成4095个id</p>
 *
 * @author wangjn
 * @date 2019/5/21
 */
@Slf4j
public class SequenceGenerator {

    /**
     * 机器号id
     */
    private long workerId;

    /**
     * 序列号类型id
     *
     * 对应snowFlake算法实现中的 datacenterId
     */
    private long typeId;

    /**
     * 初始序列号
     */
    private long sequence = 0L;

    /**
     * 开始时间戳，单位毫秒 2018-05-20 00:00:00
     *
     */
    private long twepoch = 1526745600000L;

    /**
     * 机器id所占最大位数
     */
    private long workerIdBits = 5L;

    /**
     * 类型id所占最大位数
     */
    private long typeIdIdBits = 5L;

    /**
     * 支持的最大机器数量 31
     */
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 支持的最大的类型数量，31
     */
    private long maxTypeId = -1L ^ (-1L << typeIdIdBits);

    /**
     * 序列号位数 12
     */
    private long sequenceBits = 12L;

    /**
     * 机器id的偏移位数 12
     */
    private long workerIdShift = sequenceBits;

    /**
     * typeId的偏移位数 27
     */
    private long typeIdShift = sequenceBits + workerIdBits;

    /**
     * 时间戳的偏移位数 22
     */
    private long timestampLeftShift = sequenceBits + workerIdBits + typeIdIdBits;

    /**
     * 掩码 防止序列号溢出 4095
     */
    private long sequenceMask = -1L ^ (-1L << sequenceBits);

    private long lastTimestamp = -1L;

    public SequenceGenerator(long workerId, long typeId){
        // sanity check for workerId
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (typeId > maxTypeId || typeId < 0) {
            throw new IllegalArgumentException(String.format("typeId can't be greater than %d or less than 0", maxTypeId));
        }
        log.info("worker starting. timestamp left shift {}, typeId bits {}, worker id bits {}, sequence bits {}, workerid {}",
                timestampLeftShift, typeIdIdBits, workerIdBits, sequenceBits, workerId);

        this.workerId = workerId;
        this.typeId = typeId;
    }

    public long getWorkerId(){
        return workerId;
    }

    public long getTypeId(){
        return typeId;
    }

    /**
     * 获取下一个id
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp);
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << timestampLeftShift) |
                (typeId << typeIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

    /**
     * 需要阻塞到下一毫秒
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 获取当前毫秒时间戳
     */
    private long timeGen(){
        return System.currentTimeMillis();
    }
}
