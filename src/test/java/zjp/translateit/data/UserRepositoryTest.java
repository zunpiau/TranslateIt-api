package zjp.translateit.data;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import zjp.translateit.domain.User;
import zjp.translateit.test.SpringJdbcTest;

import static org.junit.Assert.*;
import static zjp.translateit.TestConstant.*;

public class UserRepositoryTest extends SpringJdbcTest {

    @Autowired
    private UserRepository repository;
    @Autowired
    private JdbcTemplate template;

    @Test(expected = DuplicateKeyException.class)
    public void testDuplicateUser() {
        repository.saveUser(USER);
    }

    @Test
    public void testAddUser() {
        assertEquals(1, JdbcTestUtils.countRowsInTable(template, "account"));
        repository.saveUser(new User(10000000000002L, "other", "password", "other@example", 0));
        assertEquals(2, JdbcTestUtils.countRowsInTable(template, "account"));
    }

    @Test
    public void testFindUserByEmail() {
        assertTrue(repository.hasEmail(USER_EMAIL));
        assertFalse(repository.hasEmail("non-exist@example.com"));
    }

    @Test
    public void testGetUserByAccount() {
        assertNull(repository.getUserByAccount("non-exist"));
        assertEquals(USER, repository.getUserByAccount("test"));
    }

    @Test
    public void testModifyPassword() {
        assertEquals(1, repository.modifyPassword(UID, "password"));
    }
}