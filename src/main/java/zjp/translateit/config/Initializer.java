package zjp.translateit.config;

import ch.qos.logback.ext.spring.web.LogbackConfigListener;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import zjp.translateit.web.WebConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;


public class Initializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.addListener(LogbackConfigListener.class);
        servletContext.setInitParameter("webAppRootKey", "TranslateIt");
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/*"};
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
