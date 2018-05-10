package zjp.translateit.test;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import zjp.translateit.web.WebConfig;

@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public abstract class SpringMvcTest extends SpringTest {

}
