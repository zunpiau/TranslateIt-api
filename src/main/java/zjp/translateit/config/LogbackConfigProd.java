package zjp.translateit.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class LogbackConfigProd {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public static PatternLayoutEncoder encoder(LoggerContext ctx) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(ctx);
        encoder.setPattern(" %d{yy-MM-dd HH:mm:ss} [prod] [%-5level] %logger - %msg%n");
        return encoder;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public static FileAppender appender(LoggerContext ctx, PatternLayoutEncoder encoder) {
        RollingFileAppender appender = new RollingFileAppender();
        appender.setContext(ctx);
        appender.setFile("/home/wwwlogs/tra-api.log");
        appender.setImmediateFlush(true);
        SizeBasedTriggeringPolicy triggeringPolicy = new SizeBasedTriggeringPolicy();
        triggeringPolicy.setMaxFileSize(new FileSize(1024 * 1024));
        triggeringPolicy.setContext(ctx);
        triggeringPolicy.start();
        TimeBasedRollingPolicy rollingPolicy = new TimeBasedRollingPolicy();
        rollingPolicy.setMaxHistory(7);
        rollingPolicy.setParent(appender);
        rollingPolicy.setContext(ctx);
        rollingPolicy.setFileNamePattern("/home/wwwlogs/tra-api.%d{yyyy-MM-dd}.log");
        rollingPolicy.start();
        appender.setRollingPolicy(rollingPolicy);
        appender.setTriggeringPolicy(triggeringPolicy);
        appender.setEncoder(encoder);
        return appender;
    }

}
