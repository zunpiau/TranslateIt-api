package zjp.translateit.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@Import({DataConfig.class, LogbackConfig.class})
@ComponentScan(basePackages = "zjp.translateit")
public class RootConfig {

    @Bean
    public TaskExecutor threadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

}
