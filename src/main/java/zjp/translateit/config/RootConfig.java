package zjp.translateit.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DataConfig.class, LogbackConfig.class})
@ComponentScan(basePackages = "zjp.translateit")
public class RootConfig {

}
