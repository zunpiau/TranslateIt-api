package zjp.translateit.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public boolean isTokenUsed(Token token) {
        try {
            return template.queryForObject("select used from token where sign = ? ", Boolean.TYPE, token.getSign());
        } catch (EmptyResultDataAccessException e) {
            return true;
        }
    }

    public void addToken(Token token) {
        template.update("insert into token (uid, time, sign, used) values (?, ?, ?, ?)",
                token.getUid(),
                token.getTimestamp(),
                token.getSign(),
                false);
    }

    public void setTokenUsed(Token token) {
        template.update("update token set used = ? where sign = ? ",
                true,
                token.getSign());
    }

    public void setAllTokenUsed(long uid) {
        template.update("update token set used = ? where uid = ? ",
                true,
                uid);
    }
}
