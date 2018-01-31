package zjp.translateit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import zjp.translateit.web.Interceptor.LoginInterceptor;
import zjp.translateit.web.Interceptor.TokenInterceptor;
import zjp.translateit.web.Interceptor.VerifyInterceptor;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

@EnableWebMvc
@Configuration
@PropertySource(value = "classpath:application.properties")
@ComponentScan(basePackages = {"zjp.translateit.web.controller"})
public class WebConfig extends WebMvcConfigurerAdapter {

    @Value("${salt.token}")
    private String tokenSalt;

    @Value("${salt.verify}")
    private String verifySalt;

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("utf-8"));
        stringHttpMessageConverter.setSupportedMediaTypes(
                Collections.singletonList(
                        MediaType.parseMediaType(MediaType.TEXT_PLAIN_VALUE)));
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setSupportedMediaTypes(
                Collections.singletonList(
                        MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE)));
        converters.add(stringHttpMessageConverter);
        converters.add(jackson2HttpMessageConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(tokenSalt))
                .addPathPatterns("/wordbook", "/wordbook/*", "/user/inviteCode", "/token/refresh");
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/wordbook", "/wordbook/*", "/user/inviteCode");
        registry.addInterceptor(new VerifyInterceptor(verifySalt))
                .addPathPatterns("/verifyCode");
        super.addInterceptors(registry);
    }
}
