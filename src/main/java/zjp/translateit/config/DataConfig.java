package zjp.translateit.config;

import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.JedisPoolConfig;

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
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(4);
        config.setMinIdle(2);
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(config);
        connectionFactory.setClientName("translateit");
        connectionFactory.setHostName("127.0.0.1");
        connectionFactory.setPort(6379);
        return connectionFactory;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate(connectionFactory);
        template.setEnableTransactionSupport(true);
        return template;
    }

}
