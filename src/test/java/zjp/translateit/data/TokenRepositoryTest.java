package zjp.translateit.data;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import zjp.translateit.domain.Token;
import zjp.translateit.test.SpringJdbcTest;

import javax.annotation.Nullable;

import static org.junit.Assert.assertEquals;
import static zjp.translateit.TestConstant.TOKEN;
import static zjp.translateit.TestConstant.UID;

public class TokenRepositoryTest extends SpringJdbcTest {

    @Autowired
    TokenRepository repository;
    @Autowired
    JdbcTemplate template;

    @Test
    public void testUpdateToken() {
        testSaveToken();
        assertEquals(1, repository.updateToken(TOKEN, new Token(UID, 1527256284L, "newSign")));
    }

    @Test
    public void testSaveToken() {
        assertEquals(0, countRow(null));
        repository.saveToken(TOKEN);
        assertEquals(1, countRow(null));
    }

    private int countRow(@Nullable String whereClause) {
        return JdbcTestUtils.countRowsInTableWhere(template, "token", whereClause);
    }

    @Test
    public void testRemoveAll() {
        testSaveToken();
        repository.removeAll(UID);
        assertEquals(0, countRow("uid = " + UID));
    }
}