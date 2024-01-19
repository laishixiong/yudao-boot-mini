package cn.iocoder.yudao.framework.common.util;

import cn.hutool.core.util.RandomUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 最长15位雪花id
 * 可以和64版本的雪花算法一样，使用69年之久。
 * 最大值562949953421311，最长15位，传给前端时不用考虑类型转换问题。
 * 每毫秒内并发最大2^5=256，即每秒并发256000，足矣。
 */
public class SnowIdUtils {

    // TODO 临时解决2个节点获取ID的冲突问题（现在也还是有冲突的可能行），等后续优化
    private static SnowIdUtils cbnIdUtils = new SnowIdUtils(RandomUtil.randomInt(0, 8));

    /**
     * 开始时间截 (本次时间戳为：Thu Nov 04 2010 09:42:54 GMT+0800 (中国标准时间)----1288834974657L---1656543015264587776--19 )
     */
    private final long startTime = 1683803335498L;

    /**
     * 机器id所占的位数
     */
    private final long workerIdBits = 3L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 序列在id中占的位数
     */
    private final long sequenceBits = 5L;

    /**
     * 机器ID向左移12位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 时间截向左移22位(10+12)
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 工作机器ID(0~1024)
     */
    private long workerId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    //==============================Constructors=====================================

    /**
     * 构造函数
     *
     * @param workerId 工作ID (0~1024)
     */
    public SnowIdUtils(long workerId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("workerId can't be greater than %d or less than 0",
                    maxWorkerId));
        }
        this.workerId = workerId;
    }

    public SnowIdUtils() {
    }

    // ==============================Methods==========================================

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long getId() {
        long timestamp = timeGen();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            try {
                //System.out.println("睡眠了：" + (lastTimestamp - timestamp + 1) + "秒");
                Thread.sleep(lastTimestamp - timestamp + 1);
                timestamp = timeGen();
                sequence = 0L;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            // 毫秒内序列溢出
            if (sequence == 0) {
                // 阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        // 时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        // 上次生成ID的时间截
        lastTimestamp = timestamp;

        // 移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - startTime) << timestampLeftShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    public static long nextId() {
        return cbnIdUtils.getId();
    }

    public static String nextIdStr() {
        return String.valueOf(nextId());
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        Set<Long> set = new HashSet<>(1000000);
        long l = System.currentTimeMillis();
        System.out.println("开始：" + l);
        for (int i = 0; i < 1000000; i++) {
            long id = SnowIdUtils.nextId();
            if (set.contains(id)) {
                System.out.println("重复了：" + id);
            }
            set.add(id);
            System.out.println("第" + i + "个：" + id + "长度=" + String.valueOf(id).length());
        }
        System.out.println("结束：" + String.valueOf(System.currentTimeMillis() - l));
        System.out.println(set.size());
    }
}
