package zjp.translateit.util;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class RandomStringGeneratorTest {

    private RandomStringGenerator generator = new RandomStringGenerator();

    @Test
    public void testRandom() {
        final int AMOUNT = 100;
        Set<String> set = new HashSet<>(AMOUNT);
        for (int i = 0; i < AMOUNT; i++) {
            set.add(generator.generate(8));
        }
        assertEquals(AMOUNT, set.size());
    }

    @Test
    public void testLength() {
        for (int i = 1; i < 16; i++) {
            assertEquals(i, generator.generate(i).length());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        generator.generate(0);
    }
}