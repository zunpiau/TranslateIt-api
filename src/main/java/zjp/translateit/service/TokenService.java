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

    public boolean isTokenUsed(Token token) {
        return repository.isTokenUsed(token);
    }

    @Transactional
    public Token generateToken(Token tokenOld) {
        long currentTime = System.currentTimeMillis();
        String key = EncryptUtil.hash(EncryptUtil.Algorithm.SHA256, tokenOld.getUid() + tokenSalt + currentTime);
        Token token = new Token(tokenOld.getUid(), currentTime, key);
        repository.setTokenUsed(tokenOld);
        repository.addToken(token);
        return token;
    }

    public void setAllTokenUsed(int uid) {
        repository.setAllTokenUsed(uid);
    }

    public Token generateToken(int uid) {
        long currentTime = System.currentTimeMillis();
        String key = EncryptUtil.hash(EncryptUtil.Algorithm.SHA256, uid + tokenSalt + currentTime);
        Token token = new Token(uid, currentTime, key);
        repository.addToken(token);
        return token;
    }

    public boolean checkToken(Token token) {
        String str = token.getUid() + tokenSalt + token.getTimestamp();
        return token.getSign().equals(EncryptUtil.hash(EncryptUtil.Algorithm.SHA256, str));
    }

}
