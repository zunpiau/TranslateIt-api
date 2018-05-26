package zjp.translateit.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.*;

public class UidGeneratorTest {

    private static final int THREAD_COUNT = 64;
    private final UidGenerator generator = new UidGenerator();
    private final transient CountDownLatch exitLatch = new CountDownLatch(THREAD_COUNT);
    private final transient CountDownLatch generatorLatch = new CountDownLatch(THREAD_COUNT);

    @SuppressWarnings("StatementWithEmptyBody")
    @Test
    public void generate() {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        ConcurrentMap<Long, Object> map = new ConcurrentHashMap<>(128);
        Object placeholder = new Object();
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.execute(() -> {
                generatorLatch.countDown();
                while (generatorLatch.getCount() != 0)
                    ;
                map.put(generator.generate(), placeholder);
                exitLatch.countDown();
            });
        }
        while (exitLatch.getCount() != 0)
            ;
        Assert.assertEquals(THREAD_COUNT, map.size());
        executor.shutdown();
    }
}