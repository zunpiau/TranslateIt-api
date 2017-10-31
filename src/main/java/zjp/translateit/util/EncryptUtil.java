package zjp.translateit.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil {

    public static String hash(Algorithm algorithm, String str) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm.toString());
            return new BigInteger(1, md.digest(str.getBytes())).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public enum Algorithm {

        MD5("md5"),
        SHA256("SHA-256");

        private final String algorithm;

        Algorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        @Override
        public String toString() {
            return algorithm;
        }
    }
}
