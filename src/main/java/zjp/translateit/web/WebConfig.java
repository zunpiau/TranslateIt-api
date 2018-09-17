package zjp.translateit.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import zjp.translateit.web.Interceptor.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "zjp.translateit.web",
        includeFilters = @ComponentScan.Filter({ControllerAdvice.class, Controller.class}))
public class WebConfig implements WebMvcConfigurer {

    private final ManageTokenInterceptor manageTokenInterceptor;
    private final TokenInterceptor tokenInterceptor;
    private final LoginInterceptor loginInterceptor;
    private final ManageInterceptor manageInterceptor;
    private final VerifyInterceptor verifyInterceptor;

    @Autowired
    public WebConfig(ManageTokenInterceptor manageTokenInterceptor, TokenInterceptor tokenInterceptor, LoginInterceptor loginInterceptor, ManageInterceptor manageInterceptor, VerifyInterceptor verifyInterceptor) {
        this.manageTokenInterceptor = manageTokenInterceptor;
        this.tokenInterceptor = tokenInterceptor;
        this.loginInterceptor = loginInterceptor;
        this.manageInterceptor = manageInterceptor;
        this.verifyInterceptor = verifyInterceptor;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/wordbook", "/wordbook/**", "/token/refresh");
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/wordbook", "/wordbook/**");
        registry.addInterceptor(verifyInterceptor)
                .addPathPatterns("/verifyCode");
        registry.addInterceptor(manageInterceptor)
                .addPathPatterns("/manage", "/manage/**")
                .excludePathPatterns("/manage/token");
        registry.addInterceptor(manageTokenInterceptor)
                .addPathPatterns("/manage/token");
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
}
