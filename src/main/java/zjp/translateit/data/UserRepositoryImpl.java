package zjp.translateit.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import zjp.translateit.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/*
CREATE TABLE `user` (
  id       INTEGER AUTO_INCREMENT,
  name     VARCHAR(16) UNIQUE,
  password VARCHAR(128),
  email    VARCHAR(64),
  status   TINYINT,
  PRIMARY KEY (id)
)
 */

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String SELECT_USER = "select uid, name, password, email, status from user ";
    private JdbcTemplate template;

    @Autowired
    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public User findUserByName(String username) {
        try {
            return template.queryForObject(SELECT_USER + " where name = ? ", new UserRowMapper(), username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User findUserByEmail(String email) {
        try {
            return template.queryForObject(SELECT_USER + " where email = ? ", new UserRowMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int add(User user) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template).withTableName("user");
        insert.setGeneratedKeyName("id");
        HashMap<String, Object> args = new HashMap<>();
        args.put("uid", user.getUid());
        args.put("name", user.getName());
        args.put("password", user.getPassword());
        args.put("email", user.getEmail());
        args.put("status", user.getStatus());
        return insert.executeAndReturnKey(args).intValue();
    }

    @Override
    public int generateUid() {
        return template.queryForObject("SELECT uid " +
                "FROM (" +
                "  SELECT FLOOR(RAND() * 8999999) + 1000000 AS uid " +
                "  FROM user " +
                "  UNION " +
                "  SELECT FLOOR(RAND() * 9999999) + 1000000 AS uid " +
                ") AS ss " +
                "WHERE uid NOT IN (SELECT uid FROM user) " +
                "LIMIT 1 ", Integer.TYPE);
    }

    static class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getInt("uid"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getInt("status"));
        }
    }
}
