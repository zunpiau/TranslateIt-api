package zjp.translateit.util;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.time.Instant;
import java.util.Properties;

/*
  generate signature for email verify request
 */
public class EmailVerifyHelper {

    private String salt;

    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        salt = properties.getProperty("salt.verify");
    }

    @Test
    @Ignore
    public void generate() {
        long time = Instant.now().getEpochSecond();
        System.out.println("time = " + time);
        String sign = EncryptUtil.hash(EncryptUtil.Algorithm.MD5, "admin@example.com" + salt + time);
        System.out.println("sign = " + sign);
    }

}