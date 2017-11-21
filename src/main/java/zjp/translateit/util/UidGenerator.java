package zjp.translateit.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class UidGenerator {

    private final static long twepoch = 1288834974657L; //Thu, 04 Nov 2010 01:42:54 GMT
    private final static long sequenceBits = 6L;
    private final static long sequenceMask = ~(-1L << sequenceBits);
    private final static long timestampLeftShift = sequenceBits;
    private static long lastTimestamp = -1L;
    private long sequence = 0L;

    public synchronized long generate() {

        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            try {
                throw new Exception("Clock moved backwards.  Refusing to generate id for " + (lastTimestamp - timestamp) + " milliseconds");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tailNextMillis(lastTimestamp);
            }
        } else {
            sequence = new SecureRandom().nextInt(10);
        }

        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << timestampLeftShift) | sequence;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    private long tailNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

}
