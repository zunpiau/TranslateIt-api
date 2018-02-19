package zjp.translateit.web.Interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import zjp.translateit.domain.Token;
import zjp.translateit.util.EncryptUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static zjp.translateit.Constant.*;

public class TokenInterceptor extends HandlerInterceptorAdapter {

    private final String tokenSalt;

    public TokenInterceptor(String tokenSalt) {
        this.tokenSalt = tokenSalt;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) {
        String uid = httpServletRequest.getHeader(HEADER_UID);
        String timestamp = httpServletRequest.getHeader(HEADER_TIMESTAMP);
        String sign = httpServletRequest.getHeader(HEADER_SIGN);
        if (uid == null
            || timestamp == null
            || sign == null
            || !sign.equals(EncryptUtil.hash(EncryptUtil.Algorithm.SHA256, uid + tokenSalt + timestamp))) {
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        httpServletRequest.setAttribute(ATTRIBUTE_TOKEN, new Token(Long.parseLong(uid), Long.parseLong(timestamp), sign));
        return true;
    }

}
