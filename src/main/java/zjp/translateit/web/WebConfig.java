package zjp.translateit.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import zjp.translateit.web.Interceptor.LoginInterceptor;
import zjp.translateit.web.Interceptor.TokenInterceptor;
import zjp.translateit.web.Interceptor.VerifyInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@EnableWebMvc
@Configuration
@PropertySource(value = "classpath:application.properties")
@ComponentScan(basePackages = "zjp.translateit.web",
        includeFilters = @ComponentScan.Filter({RestControllerAdvice.class, RestController.class}))
public class WebConfig extends WebMvcConfigurerAdapter {

    @Value("${salt.token}")
    private String tokenSalt;
    @Value("${salt.verify}")
    private String verifySalt;
    @Value("${expire.verify}")
    private long verifyExpire;
    @Value("${expire.token}")
    private long tokenExpire;

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(tokenSalt))
                .addPathPatterns("/wordbook", "/wordbook/*", "/user/inviteCode", "/token/refresh");
        registry.addInterceptor(new LoginInterceptor(tokenExpire))
                .addPathPatterns("/wordbook", "/wordbook/*", "/user/inviteCode");
        registry.addInterceptor(new VerifyInterceptor(verifyExpire, verifySalt))
                .addPathPatterns("/verifyCode");
        super.addInterceptors(registry);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        stringHttpMessageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_PLAIN));
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        converters.add(stringHttpMessageConverter);
        converters.add(jackson2HttpMessageConverter);
    }
}
