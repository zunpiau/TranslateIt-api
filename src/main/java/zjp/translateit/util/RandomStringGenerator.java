package zjp.translateit.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomStringGenerator {

    private final SecureRandom random;
    // exclude 'i', 'l', 'o'
    private final char[] CHAR_SHEET = "abcdefghjkmnpqrstuvwxyz".toCharArray();
    private final int CHARS_LENGTH = CHAR_SHEET.length;

    public RandomStringGenerator() {
        random = new SecureRandom();
    }

    public String generate(int length) {
        if (length <= 0)
            throw new IllegalArgumentException("length must bigger than zero.");
        StringBuilder builder = new StringBuilder(length);
        while (length-- > 0) {
            builder.append(CHAR_SHEET[random.nextInt(CHARS_LENGTH)]);
        }
        return builder.toString();
    }

}
