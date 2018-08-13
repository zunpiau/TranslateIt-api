package zjp.translateit.web.Interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import zjp.translateit.util.EncryptUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

import static zjp.translateit.Constant.*;

@Component
@PropertySource(value = "classpath:application.properties")
public class VerifyInterceptor extends HandlerInterceptorAdapter {

    @Value("${salt.verify}")
    private String verifySalt;
    @Value("${expire.verify}")
    private long verifyExpire;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) {
        String email = httpServletRequest.getHeader(HEADER_EMAIL);
        String timestamp = httpServletRequest.getHeader(HEADER_TIMESTAMP);
        String sign = httpServletRequest.getHeader(HEADER_SIGN);
        if (email == null
            || timestamp == null
            || sign == null
            || !sign.equals(EncryptUtil.hash(EncryptUtil.Algorithm.MD5, email + verifySalt + timestamp))) {
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        if ((Instant.now().getEpochSecond() - Integer.parseInt(timestamp)) > verifyExpire) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return false;
        }
        return true;
    }

}
