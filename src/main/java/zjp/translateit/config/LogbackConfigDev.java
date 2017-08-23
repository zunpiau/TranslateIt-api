package zjp.translateit.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.ConsoleAppender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class LogbackConfigDev {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public static PatternLayoutEncoder encoder(LoggerContext ctx) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(ctx);
        encoder.setPattern(" %d{yy-MM-dd HH:mm:ss} [dev] [%-5level] %logger - %msg%n");
        return encoder;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public static ConsoleAppender appender(LoggerContext ctx, PatternLayoutEncoder encoder) {
        ConsoleAppender appender = new ConsoleAppender();
        appender.setContext(ctx);
        appender.setEncoder(encoder);
        return appender;
    }

}
