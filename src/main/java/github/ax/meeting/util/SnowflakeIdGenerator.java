package github.ax.meeting.util;

import org.springframework.stereotype.Component;

/**
 * 雪花算法ID生成器
 */
//该雪花ID生成器可以生成唯一的、有序的分布式ID，其中包含了时间戳、数据中心ID、工作节点ID和序列号等信息。
@Component
public class SnowflakeIdGenerator {
    private static final long START_TIMESTAMP = 1691087910202L; //起始时间戳，用于计算时间戳部分。

    private static final long DATA_CENTER_ID_BITS = 5L; //数据中心ID的位数。
    private static final long WORKER_ID_BITS = 5L; //工作节点ID的位数。
    private static final long SEQUENCE_BITS = 12L; //序列号的位数。

    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS); //数据中心ID的最大值。
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS); //工作节点ID的最大值。
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS); //序列号的最大值。

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS; //工作节点ID的位移量。
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS; //数据中心ID的位移量。
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS; //时间戳的位移量。

    //数据中心ID、工作节点ID、上一次生成ID的时间戳和序列号等属性。
    private final long dataCenterId;
    private final long workerId;
    private long lastTimestamp = -1L;
    private long sequence = 0L;

    public SnowflakeIdGenerator(){
        this(1, 1);
    }

    //构造函数中对数据中心ID和工作节点ID进行了合法性检查，并将它们赋值给对应的属性。
    private SnowflakeIdGenerator(long dataCenterId, long workerId) {
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException("Data center ID can't be greater than " + MAX_DATA_CENTER_ID + " or less than 0");
        }
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("Worker ID can't be greater than " + MAX_WORKER_ID + " or less than 0");
        }
        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
    }

    /**
     * 生成一个新的雪花算法ID加锁
     * @return 雪花ID
     */
    public synchronized long nextId() {
        long timestamp = getCurrentTimestamp();
        //首先获取当前的时间戳。如果时间戳小于上一次生成ID的时间戳，抛出异常，因为时间戳不应该后退。
        if (timestamp < lastTimestamp) {
            throw new IllegalStateException("Clock moved backwards. Refusing to generate ID.");
        }
        //如果时间戳与上一次生成ID的时间戳相同，递增序列号。如果序列号达到最大值，说明在同一毫秒内已经生成了足够多的ID，需要等待下一毫秒。
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                timestamp = getNextTimestamp(lastTimestamp);
            }
        } else { //如果时间戳与上一次生成ID的时间戳不同，重置序列号为0。
            sequence = 0L;
        }
        //更新上一次生成ID的时间戳为当前时间戳。
        lastTimestamp = timestamp;
        //根据时间戳、数据中心ID、工作节点ID和序列号，通过位运算生成最终的雪花ID。
        return ((timestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT) |
                (dataCenterId << DATA_CENTER_ID_SHIFT) |
                (workerId << WORKER_ID_SHIFT) |
                sequence;
    }

    //getCurrentTimestamp 方法用于获取当前的时间戳。
    private long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    //getNextTimestamp 方法用于获取下一个时间戳，如果当前时间戳小于等于上一次生成ID的时间戳，就一直循环获取，直到获得一个更大的时间戳。
    private long getNextTimestamp(long lastTimestamp) {
        long timestamp = getCurrentTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentTimestamp();
        }
        return timestamp;
    }
}
