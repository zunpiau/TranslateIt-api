package zjp.translateit.web.Interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import zjp.translateit.util.EncryptUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

import static zjp.translateit.Constant.AUTH_TOKEN;

public class ManageInterceptor extends HandlerInterceptorAdapter {

    private final long ROOT_EXPIRE;
    private final String ROOT_SALT;

    public ManageInterceptor(long root_expire, String root_salt) {
        ROOT_EXPIRE = root_expire;
        ROOT_SALT = root_salt;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String tokenString = request.getHeader(AUTH_TOKEN);
        if (tokenString == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                return false;
            }
            for (Cookie cookie : cookies) {
                if (AUTH_TOKEN.equals(cookie.getName())) {
                    tokenString = cookie.getValue();
                    break;
                }
            }
        }
        if (tokenString == null) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        String[] token = new String(Base64.getDecoder().decode(tokenString), StandardCharsets.UTF_8)
                .split("\\.");
        if (token.length != 3) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        String name = token[0];
        String timestamp = token[1];
        String sign = token[2];
        if (name == null
            || timestamp == null
            || sign == null
            || !sign.equals(EncryptUtil.hash(EncryptUtil.Algorithm.SHA256, name + ROOT_SALT + timestamp))) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        if ((Instant.now().getEpochSecond() - Integer.parseInt(timestamp)) > ROOT_EXPIRE) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return false;
        }
        return true;
    }
}
