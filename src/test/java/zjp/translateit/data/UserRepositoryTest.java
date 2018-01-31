package zjp.translateit.data;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import zjp.translateit.config.RootConfig;
import zjp.translateit.domain.User;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@ActiveProfiles("dev")
@WebAppConfiguration
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