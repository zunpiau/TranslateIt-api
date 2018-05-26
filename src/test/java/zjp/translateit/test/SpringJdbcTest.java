package zjp.translateit.test;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import zjp.translateit.data.UserRepository;

import javax.sql.DataSource;

import static zjp.translateit.TestConstant.USER;

@Transactional
@Rollback
public abstract class SpringJdbcTest extends SpringTest {

    @Autowired
    DataSource dataSource;
    @Value("classpath:data.sql")
    Resource script;
    @Autowired
    UserRepository repository;

    @Before
    public void setup() {
        DatabasePopulatorUtils.execute(new ResourceDatabasePopulator(script), dataSource);
        repository.saveUser(USER);
    }
}
