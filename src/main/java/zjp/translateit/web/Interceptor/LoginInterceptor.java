package zjp.translateit.web.Interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import zjp.translateit.domain.Token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

import static zjp.translateit.Constant.ATTRIBUTE_TOKEN;

@Component
@PropertySource(value = "classpath:application.properties")
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Value("${expire.token}")
    private long tokenExpire;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) {
        if ((Instant.now().getEpochSecond() - ((Token) httpServletRequest.getAttribute(ATTRIBUTE_TOKEN)).getTimestamp()) > tokenExpire) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return false;
        } else {
            return true;
        }
    }
}
