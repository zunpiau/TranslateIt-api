package zjp.translateit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zjp.translateit.data.TokenRepository;
import zjp.translateit.domain.Token;
import zjp.translateit.util.EncryptUtil;

import java.time.Instant;

@Service
@PropertySource(value = "classpath:application.properties")
public class TokenService {

    private final TokenRepository repository;
    @Value("${salt.token}")
    private String tokenSalt;

    @Autowired
    public TokenService(TokenRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Token refreshToken(Token oldToken) {
        Token token = generateToken(oldToken.getUid());
        if (repository.updateToken(oldToken, token) == 1) {
            return token;
        } else {
            repository.removeAll(oldToken.getUid());
            return null;
        }
    }

    public Token getNewToken(long uid) {
        Token token = generateToken(uid);
        repository.saveToken(token);
        return token;
    }

    private Token generateToken(long uid) {
        long currentTime = Instant.now().getEpochSecond();
        String key = EncryptUtil.hash(EncryptUtil.Algorithm.SHA256, uid + tokenSalt + currentTime);
        return new Token(uid, currentTime, key);
    }

}
