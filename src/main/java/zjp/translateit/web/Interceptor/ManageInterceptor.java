package zjp.translateit.web.Interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import zjp.translateit.util.EncryptUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

import static zjp.translateit.Constant.AUTH_TOKEN;

@Component
@PropertySource(value = "classpath:application.properties")
public class ManageInterceptor extends HandlerInterceptorAdapter {

    @Value("${manager.salt.token}")
    private String salt;
    @Value("${expire.manager}")
    private long expire;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String tokenString = request.getHeader(AUTH_TOKEN);
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
            || !sign.equals(EncryptUtil.hash(EncryptUtil.Algorithm.SHA256, name + salt + timestamp))) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        if ((Instant.now().getEpochSecond() - Integer.parseInt(timestamp)) > expire) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return false;
        }
        return super.preHandle(request, response, handler);
    }
}
