package zjp.translateit.test;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import zjp.translateit.config.RootConfig;
import zjp.translateit.service.EmailService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTest.Config.class)
@ActiveProfiles("test")
public abstract class SpringTest {

    @Configuration
    @Import(RootConfig.class)
    public static class Config {

        @Bean
        @Primary
        public EmailService emailService() {
            return Mockito.mock(EmailService.class);
        }

    }
}
