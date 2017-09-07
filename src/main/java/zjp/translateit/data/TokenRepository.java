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

    public void addToken(Token token) {
        template.update("insert into token (uid, time, sign, used) values (?, ?, ?, ?)",
                token.getUid(),
                token.getTimestamp(),
                token.getSign(),
                false);
    }

    public int replaceToken(Token oldToken, Token newToken) {
        return template.update("update token set sign = ? , time = ?  where uid = ? and sign = ? and used = false ",
                newToken.getSign(),
                newToken.getTimestamp(),
                oldToken.getUid(),
                oldToken.getSign());
    }

    public void setAllTokenUsed(long uid) {
        template.update("update token set used = ? where uid = ? ",
                true,
                uid);
    }
}
