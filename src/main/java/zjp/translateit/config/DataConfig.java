package zjp.translateit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
import zjp.translateit.util.ProfileHelper;

import javax.sql.DataSource;

@Configuration
@PropertySource(value = "classpath:application.properties")
@PropertySource(value = "classpath:application-${spring.profiles.active}.properties")
@EnableTransactionManagement
public class DataConfig {

    @Value("classpath:data.sql")
    private Resource dataScript;

    @Value("${db.user}")
    private String user;

    @Value("${db.password}")
    private String password;

    @Value("${db.url}")
    private String url;

    @Value("${db.driver}")
    private String driver;

    @Bean
    public DataSourceTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public DataSource dataSource(ProfileHelper helper) {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setInitialSize(4);
        dataSource.setMinIdle(4);
        dataSource.setMaxIdle(20);
        dataSource.setTestOnBorrow(true);
        dataSource.setRemoveAbandoned(true);
        dataSource.setValidationQuery("SELECT 1");
        if (helper.isDev()) {
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
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(10);
        config.setMinIdle(2);
        return config;
    }

    @Bean
    public RedisConnectionFactory connectionFactory(JedisPoolConfig config) {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setClientName("translateit");
        connectionFactory.setUsePool(true);
        connectionFactory.setUsePool(true);
        connectionFactory.setPoolConfig(config);
        return connectionFactory;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate(connectionFactory);
        template.setEnableTransactionSupport(true);
        return template;
    }

}
