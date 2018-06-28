package zjp.translateit.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zjp.translateit.domain.User;

import javax.annotation.Nullable;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate template;
    private final RowMapper<User> userRowMapper = (rs, rowNum) ->
            new User(rs.getLong("uid"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getInt("status"));

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
                    userRowMapper,
                    account,
                    account);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean hasEmail(String email) {
        Integer amount = template.queryForObject("SELECT COUNT(id) FROM account WHERE email = ? ::citext",
                Integer.class,
                email);
        return amount != null && amount == 1;
    }

    @Override
    public boolean hasUser(String userName) {
        Integer amount = template.queryForObject("SELECT COUNT(id) FROM account WHERE name = ? ::citext",
                Integer.class,
                userName);
        return amount != null && amount == 1;
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

}
