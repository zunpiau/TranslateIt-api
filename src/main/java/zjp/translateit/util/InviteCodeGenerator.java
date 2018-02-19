package zjp.translateit.util;

import org.springframework.stereotype.Component;

@Component
public class InviteCodeGenerator {

    private static final int RADIX = 58;
    private final char[] alphabet = "rpshnaf39wBUDNEGHJKLM4PQRST7VWXYZ2bcdeCg65jkm8oFqi1tuvAxyz".toCharArray();
    private final UidGenerator uidGenerator;

    public InviteCodeGenerator() {
        uidGenerator = new UidGenerator();
    }

    public String generate() {
        return encode(uidGenerator.generate());
    }

    private String encode(long l) {
        char[] buf = new char[65];
        int charPos = 64;
        boolean negative = (l < 0);

        if (!negative) {
            l = -l;
        }
        while (l <= -RADIX) {
            buf[charPos--] = alphabet[(int) (-(l % RADIX))];
            l = l / RADIX;
        }
        buf[charPos] = alphabet[(int) (-l)];
        if (negative) {
            buf[--charPos] = '-';
        }
        return new String(buf, charPos, (65 - charPos));
    }

}
