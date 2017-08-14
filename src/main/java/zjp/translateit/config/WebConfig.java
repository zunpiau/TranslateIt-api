package zjp.translateit.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"zjp.translateit.web", "zjp.translateit.web"})
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        ArrayList<MediaType> plainText = new ArrayList<>(2);
        plainText.add(MediaType.parseMediaType(MediaType.TEXT_PLAIN + ";charset=utf-8"));
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("utf-8"));
        stringHttpMessageConverter.setSupportedMediaTypes(plainText);
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ArrayList<MediaType> mediaTypes = new ArrayList<>(2);
        mediaTypes.add(MediaType.parseMediaType("application/json;charset=utf-8"));
        jackson2HttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        converters.add(stringHttpMessageConverter);
        converters.add(jackson2HttpMessageConverter);
    }
}
