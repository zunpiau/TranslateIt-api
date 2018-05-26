package zjp.translateit.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import zjp.translateit.domain.Token;
import zjp.translateit.test.SpringJdbcTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static zjp.translateit.TestConstant.UID;

public class TokenServiceTest extends SpringJdbcTest {

    @Autowired
    TokenService service;
    @Autowired
    JdbcTemplate template;

    @Test
    public void testRefreshToken() {
        Token token = service.getNewToken(UID);
        Token nonexistentToken = new Token(token.getUid(), token.getTimestamp() - 10, "nonexistent");
        assertNull(service.refreshToken(nonexistentToken));
        assertEquals(0, JdbcTestUtils.countRowsInTable(template, "token"));
    }
}