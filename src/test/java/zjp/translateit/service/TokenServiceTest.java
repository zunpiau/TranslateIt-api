package zjp.translateit.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import zjp.translateit.domain.Token;
import zjp.translateit.test.SpringJdbcTest;

import static org.junit.Assert.*;
import static zjp.translateit.TestConstant.UID;

public class TokenServiceTest extends SpringJdbcTest {

    @Autowired
    TokenService service;
    @Autowired
    JdbcTemplate template;

    @Test
    public void testGetNewToken() {
        assertEquals(0, JdbcTestUtils.countRowsInTable(template, "token"));
        service.getNewToken(UID);
        assertEquals(1, JdbcTestUtils.countRowsInTable(template, "token"));
    }

    @Test
    public void testRefreshToken() {
        Token oldToken = service.getNewToken(UID);
        Token newToken = service.refreshToken(oldToken);
        assertNotNull(newToken);
        assertEquals(UID, newToken.getUid());
        assertEquals(1, JdbcTestUtils.countRowsInTable(template, "token"));

        // must modify timestamp
        Token nonexistentToken = new Token(UID, oldToken.getTimestamp() - 10, oldToken.getSign());
        assertNull(service.refreshToken(nonexistentToken));
        assertEquals(0, JdbcTestUtils.countRowsInTable(template, "token"));
    }
}