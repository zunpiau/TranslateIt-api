package zjp.translateit;

import zjp.translateit.domain.User;

public class TestConstant {

    public static final long UID = 10000000000001L;
    public static final String USER_NAME = "test";
    public static final String USER_EMAIL = "test@example.com";
    public static final String USER_PASSWORD = "passwd";
    public static final String USER_PASSWORD_HASHED = "$2a$10$dUfp2uqdJLTb";
    public static final User USER = new User(UID,
            USER_NAME,
            USER_PASSWORD_HASHED,
            USER_EMAIL,
            0);

}
