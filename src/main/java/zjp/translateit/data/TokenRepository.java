package zjp.translateit.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import zjp.translateit.domain.Token;

@Repository
public class TokenRepository {

    private final JdbcTemplate template;

    @Autowired
    public TokenRepository(JdbcTemplate template) {
        this.template = template;
    }

    public void saveToken(Token token) {
        template.update("INSERT INTO token (uid, time, sign) VALUES (?, ?, ?)",
                token.getUid(),
                token.getTimestamp(),
                token.getSign());
    }

    public int updateToken(Token oldToken, Token newToken) {
        return template.update("UPDATE token SET sign = ? , time = ? " +
                               " WHERE uid = ? AND time = ? ",
                newToken.getSign(),
                newToken.getTimestamp(),
                oldToken.getUid(),
                oldToken.getTimestamp());
    }

    public void removeAll(long uid) {
        template.update("DELETE FROM token WHERE uid = ? ", uid);
    }
}
