package zjp.translateit.data;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import zjp.translateit.domain.User;
import zjp.translateit.test.SpringJdbcTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserRepositoryTest extends SpringJdbcTest {

    @Autowired
    UserRepository repository;
    private User user = new User(14234143336134L,
            "xxx",
            "passwd",
            "xxx@test.com",
            0);

    @Test
    public void testAdd() {
        repository.saveUser(user);
    }

    @Test
    public void testFindUserByEmail() {
        testAdd();
        assertTrue(repository.hasEmail("xxx@test.com"));
        assertFalse(repository.hasEmail("non-exist@test.com"));
    }
}