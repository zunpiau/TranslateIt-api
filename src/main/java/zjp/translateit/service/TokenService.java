package zjp.translateit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zjp.translateit.data.TokenRepository;
import zjp.translateit.domain.Token;
import zjp.translateit.util.EncryptUtil;

@Service
@PropertySource(value = "classpath:application.properties")
@PropertySource(value = "classpath:application-${spring.profiles.active}.properties")
public class TokenService {

    private final TokenRepository repository;
    @Value("${salt.token}")
    private String tokenSalt;

    @Autowired
    public TokenService(TokenRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Token getNewToken(Token oldToken) {
        Token token = generateToken(oldToken.getUid());
        int i = repository.replaceToken(oldToken, token);
        if (i == 1)
            return token;
        else {
            repository.setAllTokenUsed(oldToken.getUid());
            return null;
        }
    }

    private Token generateToken(int uid) {
        long currentTime = System.currentTimeMillis();
        String key = EncryptUtil.hash(EncryptUtil.Algorithm.SHA256, uid + tokenSalt + currentTime);
        return new Token(uid, currentTime, key);
    }

    public Token getNewToken(int uid) {
        Token token = generateToken(uid);
        repository.addToken(token);
        return token;
    }

    public boolean checkToken(Token token) {
        String str = token.getUid() + tokenSalt + token.getTimestamp();
        return token.getSign().equals(EncryptUtil.hash(EncryptUtil.Algorithm.SHA256, str));
    }

}
