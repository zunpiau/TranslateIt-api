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

    private static final String SELECT_USER = "SELECT uid, name, password, email, status FROM user ";
    private JdbcTemplate template;

    @Autowired
    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    @Nullable
    @Override
    public User getUserByName(String username) {
        try {
            return template.queryForObject(SELECT_USER + " WHERE name = ? ",
                    new UserRowMapper(),
                    username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            return template.queryForObject(SELECT_USER + " WHERE email = ? ",
                    new UserRowMapper(),
                    email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void saveUser(User user) {
        template.update("INSERT INTO user (uid, name, password, email, status) VALUE (?, ?, ?, ?, ?)",
                user.getUid(),
                user.getName(),
                user.getPassword(),
                user.getEmail(),
                user.getStatus());
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
