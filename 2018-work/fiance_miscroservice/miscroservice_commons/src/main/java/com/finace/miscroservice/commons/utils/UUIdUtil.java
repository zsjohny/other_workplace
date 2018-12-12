package com.finace.miscroservice.commons.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * uuid的自增工具
 */
public class UUIdUtil {

    private UUIdUtil() {

    }


    private static final AtomicLong atomicLong = new AtomicLong();


    /**
     * 生成自增Id
     *
     * @return
     */
    public static String generateUuid() {

        return String.valueOf(IdGen.get().nextId());


    }

    /**
     * 生成无序姓名
     *
     * @return
     */
    public static String generateName() {

        return new StringBuffer(Instant.now().toString().replaceAll("[-:]", "").replaceAll("[.TZ]", "")).append(UUID.randomUUID().toString()).reverse().toString();
    }

    private static LongAdder _orderNoLock = new LongAdder();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    private static final int WAITTING_TIME = 500;
    private static final int MAX_ORDER_LENGTH = 30;
    private static final int MAX_USER_LENGTH = 11;
    private static final int ORDERNO_HOLD_LOCK = 1;
    private static ThreadLocalRandom random = ThreadLocalRandom.current();
    private static final int CUT_LENGTH = 4;
    private static final int INITLIAZE_ORDERS_NUM = 0;
    private static final int ENDWITH_ORDERS_NUM = 9;
    private static final int DEFAULT_ORDER_PREFIX = 0;


    /**
     * 生成订单号
     *
     * @return
     */
    public static String generateOrderNo() {
        return increaseGenerateOrderNo(DEFAULT_ORDER_PREFIX, null);

    }


    /**
     * 根据用户Id生成订单号
     *
     * @return
     */
    public static String generateOrderNo(String orderNo) {

        return increaseGenerateOrderNoVersion(orderNo);

    }


    /**
     * 增长的订单版本信息
     *
     * @param orderNo 订单号
     * @return
     */
    private static String increaseGenerateOrderNoVersion(String orderNo) {
        String _newOrderNo = orderNo;

        if (StringUtils.isEmpty(orderNo) || orderNo.length() < MAX_ORDER_LENGTH) {

            return _newOrderNo;
        }


        int _incVersion = Integer.parseInt(orderNo.substring(orderNo.length() - 1, orderNo.length()));
        _incVersion++;
        _newOrderNo = orderNo.substring(0, orderNo.length() - 1) + _incVersion;

        if (_incVersion > ENDWITH_ORDERS_NUM) {
            return orderNo;
        } else {
            return _newOrderNo;
        }


    }


    /**
     * 自增长的订单内容
     *
     * @param prefix 订单前缀
     * @param uid    用户ID
     * @return
     */
    private static String increaseGenerateOrderNo(int prefix, Integer uid) {
        _orderNoLock.increment();
        while (_orderNoLock.intValue() == ORDERNO_HOLD_LOCK) {

            StringBuilder builder = new StringBuilder(String.valueOf(prefix));
            LocalDateTime time = LocalDateTime.now();
            String format = time.format(formatter);

            String id = UUID.randomUUID().toString().replaceAll("-", "").replaceAll("[a-zA-Z]+", "");
            if (uid != null) {
                String uidPrefix;
                if (uid.toString().length() > CUT_LENGTH) {
                    uidPrefix = uid.toString().substring(0, CUT_LENGTH);
                } else {
                    uidPrefix = String.format("%04d", uid);
                }
                builder.append(uidPrefix);
            }
            builder.append(format);
            builder.append(id);


            if (builder.length() < MAX_ORDER_LENGTH) {
                int len = MAX_ORDER_LENGTH - builder.length();
                for (int i = 0; i < len; i++) {
                    builder.append(random.nextInt(10));
                }
            } else {
                builder.delete(MAX_ORDER_LENGTH, builder.length());

            }
            builder.append(INITLIAZE_ORDERS_NUM);
            _orderNoLock.reset();
            return builder.toString();


        }

        try {
            Thread.sleep(WAITTING_TIME);
        } catch (InterruptedException e) {

        }

        return increaseGenerateOrderNo(prefix, uid);
    }


    static class IdGen {
        private long workerId;
        private long datacenterId;
        private long sequence = 0L;
        private long twepoch = 1288834974657L;
        private long workerIdBits = 5L;
        //节点ID长度
        private long datacenterIdBits = 5L;
        //数据中心ID长度
        private long maxWorkerId = -1L ^ (-1L << workerIdBits);
        //最大支持机器节点数0~31，一共32个
        private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
        //最大支持数据中心节点数0~31，一共32个
        private long sequenceBits = 12L;
        //序列号12位
        private long workerIdShift = sequenceBits;
        //机器节点左移12位
        private long datacenterIdShift = sequenceBits + workerIdBits;
        //数据中心节点左移17位
        private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
        //时间毫秒数左移22位
        private long sequenceMask = -1L ^ (-1L << sequenceBits);
        //最大为4095
        private long lastTimestamp = -1L;

        private static class IdGenHolder {
            private static final IdGen instance = new IdGen();
        }

        public static IdGen get() {
            return IdGenHolder.instance;
        }

        public IdGen() {
            this(0L, 0L);
        }

        public IdGen(long workerId, long datacenterId) {
            if (workerId > maxWorkerId || workerId < 0) {
                throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
            }
            if (datacenterId > maxDatacenterId || datacenterId < 0) {
                throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
            }
            this.workerId = workerId;
            this.datacenterId = datacenterId;
        }

        public synchronized long nextId() {
            long timestamp = timeGen();
            //获取当前毫秒数
            //如果服务器时间有问题(时钟后退) 报错。
            if (timestamp < lastTimestamp) {
                throw new RuntimeException(String.format(
                        "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
            }
            //如果上次生成时间和当前时间相同,在同一毫秒内
            if (lastTimestamp == timestamp) {
                //sequence自增，因为sequence只有12bit，所以和sequenceMask相与一下，去掉高位
                sequence = (sequence + 1) & sequenceMask;
                //判断是否溢出,也就是每毫秒内超过4095，当为4096时，与sequenceMask相与，sequence就等于0
                if (sequence == 0) {
                    timestamp = tilNextMillis(lastTimestamp);
                    //自旋等待到下一毫秒
                }
            } else {
                sequence = 0L;
                //如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加
            }
            lastTimestamp = timestamp;
            // 最后按照规则拼出ID。
            // 000000000000000000000000000000000000000000  00000            00000       000000000000
            // time                                      datacenterId      workerId     sequence
            // return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
            //        | (workerId << workerIdShift) | sequence;

            long longStr = ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
            // System.out.println(longStr);
            return longStr;
        }

        protected long tilNextMillis(long lastTimestamp) {
            long timestamp = timeGen();
            while (timestamp <= lastTimestamp) {
                timestamp = timeGen();
            }
            return timestamp;
        }

        protected long timeGen() {
            return System.currentTimeMillis();
        }

    }


}
