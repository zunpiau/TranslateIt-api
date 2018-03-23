package zjp.translateit.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zjp.translateit.domain.User;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate template;

    @Autowired
    public UserRepositoryImpl(JdbcTemplate template) {
        this.template = template;
    }

    @Nullable
    @Override
    public User getUserByAccount(String account) {
        try {
            return template.queryForObject("SELECT uid, name, password, email, status FROM account "
                                           + " WHERE email = ? OR name = ?",
                    new UserRowMapper(),
                    account,
                    account);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean hasEmail(String email) {
        return template.queryForObject("SELECT COUNT(id) FROM account WHERE email = ? ",
                Integer.class,
                email) == 1;
    }

    @Override
    public void saveUser(User user) {
        template.update("INSERT INTO account (uid, name, password, email, status)" +
                        " VALUES (?, ?, ?, ?, ?)",
                user.getUid(),
                user.getName(),
                user.getPassword(),
                user.getEmail(),
                user.getStatus());
    }

    @Override
    public int modifyPassword(long uid, String passwordSalted) {
        return template.update("UPDATE account SET password = ? WHERE uid = ? ", passwordSalted, uid);
    }

    static class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getLong("uid"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getInt("status"));
        }
    }
}
