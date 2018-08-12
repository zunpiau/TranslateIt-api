package zjp.translateit.web.Interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import zjp.translateit.util.EncryptUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@PropertySource(value = "classpath:application.properties")
public class ManageTokenInterceptor extends HandlerInterceptorAdapter {

    @Value("${manager.name}")
    private String managerName;
    @Value("${manager.password.hashed}")
    private String passwordHashed;
    @Value("${manager.salt.account}")
    private String salt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        if (managerName.equals(name)
            && password != null
            && passwordHashed.equals(EncryptUtil.hash(EncryptUtil.Algorithm.SHA256, name + salt + password))) {
            return true;
        } else {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
    }
}
