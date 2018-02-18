package zjp.translateit.config;

import org.springframework.context.annotation.*;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@Import({DataConfig.class, LogbackConfig.class})
@ComponentScan(basePackages = {"zjp.translateit"},
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
                pattern = {"zjp.translateit.web"}))
public class RootConfig {

    @Bean
    public TaskExecutor threadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

}
