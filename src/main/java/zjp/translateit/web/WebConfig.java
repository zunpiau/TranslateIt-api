package zjp.translateit.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import zjp.translateit.web.Interceptor.LoginInterceptor;
import zjp.translateit.web.Interceptor.ManageInterceptor;
import zjp.translateit.web.Interceptor.TokenInterceptor;
import zjp.translateit.web.Interceptor.VerifyInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EnableWebMvc
@Configuration
@PropertySource(value = "classpath:application.properties")
@ComponentScan(basePackages = "zjp.translateit.web",
        includeFilters = @ComponentScan.Filter({ControllerAdvice.class, Controller.class}))
public class WebConfig extends WebMvcConfigurerAdapter {

    @Value("${salt.token}")
    private String tokenSalt;
    @Value("${salt.verify}")
    private String verifySalt;
    @Value("${expire.verify}")
    private long verifyExpire;
    @Value("${expire.token}")
    private long tokenExpire;
    @Value("${salt.token.manager}")
    private String rootSalt;
    @Value("${expire.manager}")
    private long rootExpire;

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(tokenSalt))
                .addPathPatterns("/wordbook", "/wordbook/*", "/token/refresh");
        registry.addInterceptor(new LoginInterceptor(tokenExpire))
                .addPathPatterns("/wordbook", "/wordbook/*");
        registry.addInterceptor(new VerifyInterceptor(verifyExpire, verifySalt))
                .addPathPatterns("/verifyCode");
        registry.addInterceptor(new ManageInterceptor(rootExpire, rootSalt))
                .addPathPatterns("/manage", "/manage/*")
                .excludePathPatterns("/manage/token");
        super.addInterceptors(registry);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        ArrayList<MediaType> mediaTypes = new ArrayList<>(2);
        mediaTypes.add(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8));
        mediaTypes.add(new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8));
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        stringHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        converters.add(stringHttpMessageConverter);
        converters.add(jackson2HttpMessageConverter);
    }

    @Bean
    public ViewResolver viewResolver() {
        return new InternalResourceViewResolver("/view/", ".html");
    }
}
