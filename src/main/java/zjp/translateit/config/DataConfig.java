package zjp.translateit.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.Properties;

@Configuration
@PropertySource(value = "classpath:application.properties")
@EnableTransactionManagement
public class DataConfig {

    @Value("${spring.profiles.active}")
    private String profile;

    @Bean
    public DataSourceTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "jdbcProperties")
    public PropertiesFactoryBean jdbcProperties() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocations(new ClassPathResource(
                MessageFormat.format("jdbc-{0}.properties", profile)));
        return bean;
    }

    @Bean
    public DataSource dataSource(Properties jdbcProperties,
            @Value("classpath:data.sql") Resource dataScript) throws Exception {
        DataSource dataSource = new DataSourceFactory().createDataSource(jdbcProperties);
        if ("dev".equals(profile)) {
            DatabasePopulatorUtils.execute(new ResourceDatabasePopulator(dataScript),
                    dataSource);
        }
        return dataSource;
    }

    @Bean
    public JdbcTemplate userJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public RedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration("localhost", 6379);
        standaloneConfiguration.setDatabase(0);
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMinIdle(1);
        poolConfig.setMaxIdle(2);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setTestOnBorrow(true);
        LettucePoolingClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .build();
        return new LettuceConnectionFactory(standaloneConfiguration, clientConfiguration);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate(connectionFactory);
        template.setEnableTransactionSupport(true);
        return template;
    }

}
