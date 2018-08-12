package zjp.translateit.test;

import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Rollback
public abstract class SpringJdbcTest extends SpringTest {

}
