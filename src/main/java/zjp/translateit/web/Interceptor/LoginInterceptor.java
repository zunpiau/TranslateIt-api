package zjp.translateit.web.Interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import zjp.translateit.domain.Token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

import static zjp.translateit.Constant.ATTRIBUTE_TOKEN;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    private final long TOKEN_EXPIRE;

    public LoginInterceptor(long tokenExpire) {
        TOKEN_EXPIRE = tokenExpire;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) {
        if ((Instant.now().getEpochSecond() - ((Token) httpServletRequest.getAttribute(ATTRIBUTE_TOKEN)).getTimestamp()) > TOKEN_EXPIRE) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return false;
        } else {
            return true;
        }
    }
}
