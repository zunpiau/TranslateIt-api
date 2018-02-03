package zjp.translateit.web.Interceptor;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import zjp.translateit.util.EncryptUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

import static zjp.translateit.Constant.*;

public class VerifyInterceptor extends HandlerInterceptorAdapter {

    private final static long REQUEST_EXPIRE = 60;
    private String verifySalt;

    public VerifyInterceptor(String verifySalt) {
        this.verifySalt = verifySalt;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        LoggerFactory.getLogger(VerifyInterceptor.class).info("VerifyInterceptor.preHandle()");
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
        if ((Instant.now().getEpochSecond() - Integer.parseInt(timestamp)) > REQUEST_EXPIRE) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return false;
        }
        return true;
    }

}
