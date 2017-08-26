package zjp.translateit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zjp.translateit.data.TokenRepository;
import zjp.translateit.util.EncryptUtil;
import zjp.translateit.web.domain.Token;

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

    public boolean isTokenUsed(Token token) {
        return repository.isTokenUsed(token);
    }

    @Transactional
    public Token generateToken(Token tokenOld) {
        long currentTime = System.currentTimeMillis();
        String key = EncryptUtil.getMD5(tokenOld.getId() + tokenSalt + currentTime);
        Token token = new Token(tokenOld.getId(), currentTime, key);
        repository.setTokenUsed(tokenOld);
        repository.addToken(token);
        return token;
    }

    public void setAllTokenUsed(long uid) {
        repository.setAllTokenUsed(uid);
    }

    public Token generateToken(long uid) {
        long currentTime = System.currentTimeMillis();
        String key = EncryptUtil.getMD5(uid + tokenSalt + currentTime);
        Token token = new Token(uid, currentTime, key);
        repository.addToken(token);
        return token;
    }

    public boolean checkToken(Token token) {
        String str = token.getId() + tokenSalt + token.getTimestamp();
        return token.getSign().equals(EncryptUtil.getMD5(str));
    }

}
