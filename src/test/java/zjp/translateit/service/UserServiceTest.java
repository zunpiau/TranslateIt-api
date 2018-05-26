package zjp.translateit.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zjp.translateit.test.SpringJdbcTest;
import zjp.translateit.web.exception.UserExistException;
import zjp.translateit.web.request.RegisterRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static zjp.translateit.TestConstant.*;

public class UserServiceTest extends SpringJdbcTest {

    @Autowired
    UserService userService;

    @Test
    public void testGetUser() {
        assertEquals(USER, userService.getUser(USER_NAME, USER_PASSWORD));
        assertNull(userService.getUser(USER_NAME, "wrong passwd"));
    }

    @Test(expected = UserExistException.class)
    public void testDuplicateUser() {
        userService.registerUser(new RegisterRequest(USER_NAME, USER_PASSWORD, "verifyCode", USER_EMAIL));
    }
}