package zjp.translateit.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.*;

public class UidGeneratorTest {

    private static final int THREAD_COUNT = 64;
    private final UidGenerator generator = new UidGenerator();
    private final transient CountDownLatch exitLatch = new CountDownLatch(THREAD_COUNT);
    private final transient CyclicBarrier cyclicBarrier = new CyclicBarrier(THREAD_COUNT);

    @Test
    public void generate() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        Set<Long> set = Collections.newSetFromMap(new ConcurrentHashMap<>(THREAD_COUNT));
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.execute(() -> {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                set.add(generator.generate());
                exitLatch.countDown();
            });
        }
        exitLatch.await();
        Assert.assertEquals(THREAD_COUNT, set.size());
        executor.shutdown();
    }
}
