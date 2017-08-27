package zjp.translateit.config;

import ch.qos.logback.ext.spring.web.LogbackConfigListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@PropertySource(value = "classpath:application.properties")
public class Initializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.setInitParameter("spring.profiles.active", env);
        servletContext.setInitParameter("spring.profiles.default", env);
        servletContext.setInitParameter("spring.liveBeansView.mbeanDomain", env);
        servletContext.addListener(LogbackConfigListener.class);
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/api/*"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }
}
