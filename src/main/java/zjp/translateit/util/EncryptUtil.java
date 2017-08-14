package zjp.translateit.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil {

    public static String getMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            return new BigInteger(1, md.digest(str.getBytes())).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


}
