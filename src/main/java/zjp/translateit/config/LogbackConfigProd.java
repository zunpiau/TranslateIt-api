package zjp.translateit.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
@Profile("prod")
public class LogbackConfigProd {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public static RollingFileAppender<ILoggingEvent> appender(LoggerContext ctx, PatternLayoutEncoder encoder, Environment env) {
        RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
        appender.setContext(ctx);
        TimeBasedRollingPolicy rollingPolicy = new TimeBasedRollingPolicy();
        rollingPolicy.setMaxHistory(3);
        rollingPolicy.setParent(appender);
        rollingPolicy.setContext(ctx);
        rollingPolicy.setCleanHistoryOnStart(true);
        rollingPolicy.setFileNamePattern(env.getProperty("log.fileNamePattern"));
        rollingPolicy.start();
        appender.setRollingPolicy(rollingPolicy);
        appender.setEncoder(encoder);
        return appender;
    }

}
