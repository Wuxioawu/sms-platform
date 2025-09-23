package com.peng.sms.util;


import com.peng.sms.enums.ExceptionEnums;
import com.peng.sms.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

/**
 * Snowflake Algorithm for Generating Globally Unique IDs
 * The ID is represented by a 64-bit long value, divided as follows:
 * 1. Sign bit (1 bit): Always set to 0 to ensure the ID is a positive number.
 * 2. Timestamp (41 bits): Represents the timestamp in milliseconds for trend-increasing IDs.
 * 3. Machine ID (5 bits): Identifies the machine generating the ID.
 * 4. Service ID (5 bits): Identifies the service instance generating the ID.
 * 5. Sequence Number (12 bits): Auto-incremented within the same millisecond to avoid collisions.
 * <p>
 * This structure ensures high-performance, conflict-free unique ID generation in distributed systems.
 */
public class SnowFlakeUtil {
    /**
     * The 41-bit timestamp field can store up to 69.7 years of time values, starting from 0.
     * If using the default epoch (starting from 1970), it will last until around the year 2039.
     * If using a custom epoch (e.g., starting from 2022-11-11), it will last until nearly the year 2092.
     */
    private long timeStart = 1668096000000L;

    @Value("${snowflake.machineId:0}")
    private long machineId;

    @Value("${snowflake.serviceId:0}")
    private long serviceId;

    private long sequence;

    private long machineIdBits = 5L;

    private long serviceIdBits = 5L;

    private long sequenceBits = 12L;

    private long maxMachineId = ~(-1 << machineIdBits);

    private long maxServiceId = ~(-1 << serviceIdBits);

    @PostConstruct
    public void init() {
        if (machineId > maxMachineId || serviceId > maxServiceId) {
            System.out.println("the machine id is out of range or the service id is out of range");
            throw new ApiException(ExceptionEnums.SNOWFLAKE_OUT_OF_RANGE);
        }
    }

    private long serviceIdShift = sequenceBits;

    private long machineIdShift = sequenceBits + serviceIdBits;

    private long timestampShift = sequenceBits + serviceIdBits + machineIdBits;

    private long maxSequenceId = ~(-1 << sequenceBits);

    private long lastTimestamp = -1;

    private long timeGen() {
        return System.currentTimeMillis();
    }

    public synchronized long nextId() {
        //1、 get the current system time
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            System.out.println("current time is out of range");
            throw new ApiException(ExceptionEnums.SNOWFLAKE_TIME_BACK);
        }

        // 2. Check if the current ID generation timestamp is the same as the last one
        if (timestamp == lastTimestamp) {
            // Generating ID within the same millisecond
            sequence = (sequence + 1) & maxSequenceId;
            // 0000 10100000 :sequence
            // 1111 11111111 :maxSequenceId
            if (sequence == 0) {
                // If we enter this block, it means the sequence has reached its maximum value
                // Wait until the next millisecond to generate the next ID
                timestamp = timeGen();
                while (timestamp <= lastTimestamp) {
                    // Time has not advanced yet
                    timestamp = timeGen();
                }
            }
        } else {
            // Generating ID at a new timestamp
            sequence = 0;
        }

        // 3. Update lastTimestamp with the current timestamp
        lastTimestamp = timestamp;

        // 4. Calculate the final ID by combining all parts:
        // 41 bits for timestamp, 5 bits for machine ID, 5 bits for service ID, 12 bits for sequence
        return ((timestamp - timeStart) << timestampShift) |
                (machineId << machineIdShift) |
                (serviceId << serviceIdShift) |
                sequence & Long.MAX_VALUE;
    }
}
