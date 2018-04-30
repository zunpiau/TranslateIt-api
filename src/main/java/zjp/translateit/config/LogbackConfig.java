package zjp.translateit.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.ext.spring.ApplicationContextHolder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:application.properties")
public class LogbackConfig {

    @Bean
    public static ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }

    @Bean
    public static LoggerContext loggerContext() {
        return (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public static PatternLayoutEncoder encoder(LoggerContext ctx) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(ctx);
        encoder.setPattern("%d{yy-MM-dd HH:mm:ss.SSS} [%-5level] [%thread] %logger - %msg%n");
        return encoder;
    }

    @Profile("dev")
    @Bean(initMethod = "start", destroyMethod = "stop", name = "appender")
    public static ConsoleAppender<ILoggingEvent> consoleAppender(LoggerContext ctx, PatternLayoutEncoder encoder) {
        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setContext(ctx);
        appender.setEncoder(encoder);
        return appender;
    }

    @Profile("!dev")
    @Bean(initMethod = "start", destroyMethod = "stop", name = "appender")
    public static RollingFileAppender<ILoggingEvent> fileAppender(LoggerContext ctx,
            PatternLayoutEncoder encoder,
            @Value("${log.history}") int history,
            @Value("${log.fileNamePattern}") String parretn) {
        RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
        appender.setContext(ctx);
        TimeBasedRollingPolicy rollingPolicy = new TimeBasedRollingPolicy();
        rollingPolicy.setMaxHistory(history);
        rollingPolicy.setParent(appender);
        rollingPolicy.setContext(ctx);
        rollingPolicy.setCleanHistoryOnStart(true);
        rollingPolicy.setFileNamePattern(parretn);
        rollingPolicy.start();
        appender.setRollingPolicy(rollingPolicy);
        appender.setEncoder(encoder);
        return appender;
    }
}
