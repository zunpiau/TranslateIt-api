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

    private static final String SELECT_USER = "select id, name, password, email, status from user ";
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
    public User findUserByNameAndPassword(String username, String passwordEncrypted) {
        try {
            return template.queryForObject(SELECT_USER + " where name = ? and password = ? ",
                    new UserRowMapper(),
                    username,
                    passwordEncrypted);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User add(User user) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template).withTableName("user");
        insert.setGeneratedKeyName("id");
        HashMap<String, Object> args = new HashMap<>(6);
        args.put("name", user.getName());
        args.put("password", user.getPassword());
        args.put("email", user.getEmail());
        args.put("status", User.STATUS.NORMAL);
        long id = insert.executeAndReturnKey(args).longValue();
        return User.setUserId(id, user);
    }

    @Override
    public void updateUserStatus(long id, int status) {
        template.update("update user set status = ? where id = ?", status, id);
    }

    static class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getInt("status"));
        }
    }
}
