package zjp.translateit.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import zjp.translateit.domain.User;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

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
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template).withTableName("user");
        HashMap<String, Object> args = new HashMap<>(6);
        args.put("uid", user.getUid());
        args.put("name", user.getName());
        args.put("password", user.getPassword());
        args.put("email", user.getEmail());
        args.put("status", user.getStatus());
        insert.execute(args);
    }

    @Override
    public int generateUid() {
        return template.queryForObject("SELECT uid " +
                                       " FROM (" +
                                       "  SELECT FLOOR(RAND() * 80000000) + 10000000 AS uid " +
                                       "  FROM user " +
                                       "  UNION " +
                                       "  SELECT FLOOR(RAND() * 80000000) + 10000000 AS uid " +
                                       " ) AS ss " +
                                       " WHERE uid NOT IN (SELECT uid FROM user) " +
                                       " LIMIT 1 ",
                Integer.TYPE);
    }

    static class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getInt("uid"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getInt("status"));
        }
    }
}
