package zjp.translateit.data;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import zjp.translateit.SpringMvcTest;
import zjp.translateit.domain.User;

import static org.junit.Assert.assertEquals;

@SpringMvcTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserRepositoryTest {

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
    public void testFindUserByName() {
        assertEquals(user, repository.getUserByName("xxx"));
    }

    @Test
    public void testFindUserByEmail() {
        assertEquals(user, repository.getUserByEmail("xxx@test.com"));
    }
}