package zjp.translateit;

import org.springframework.security.crypto.bcrypt.BCrypt;
import zjp.translateit.domain.Token;
import zjp.translateit.domain.User;

public class TestConstant {

    public static final long UID = 10000000000001L;
    public static final Token TOKEN = new Token(UID, 1527256284L, "sign");
    public static final String USER_NAME = "test";
    public static final String USER_EMAIL = "test@example.com";
    public static final String USER_PASSWORD = "passwd";
    public static final String USER_PASSWORD_HASHED = BCrypt.hashpw(USER_PASSWORD, BCrypt.gensalt(10));
    public static final User USER = new User(UID,
            USER_NAME,
            USER_PASSWORD_HASHED,
            USER_EMAIL,
            0);

}
