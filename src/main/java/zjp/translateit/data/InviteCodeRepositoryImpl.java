package zjp.translateit.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zjp.translateit.domain.InviteCode;
import zjp.translateit.dto.InviteCodeDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InviteCodeRepositoryImpl implements InviteCodeRepository {

    private final JdbcTemplate template;

    @Autowired
    public InviteCodeRepositoryImpl(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public int generateCode() {
        return template.queryForObject("SELECT code " +
                                       " FROM (" +
                                       "  SELECT FLOOR(RAND() * 80000000) + 10000000 AS code " +
                                       "  FROM invite_code " +
                                       "  UNION " +
                                       "  SELECT FLOOR(RAND() * 80000000) + 10000000 AS code " +
                                       " ) AS ss " +
                                       " WHERE code NOT IN (SELECT code FROM invite_code) " +
                                       " LIMIT 1 ",
                Integer.TYPE);
    }

    @Override
    public void saveInviteCode(List<InviteCode> inviteCodes) {
        template.batchUpdate("INSERT INTO invite_code (uid, code) VALUE (?, ? )",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, inviteCodes.get(i).getUid());
                        ps.setInt(2, inviteCodes.get(i).getCode());
                    }

                    @Override
                    public int getBatchSize() {
                        return inviteCodes.size();
                    }
                });
    }

    @Override
    public List<InviteCodeDto> listInviteCode(int uid) {
        return template.query("SELECT code, time_modified, user FROM invite_code WHERE uid = ? ",
                new RowMapper<InviteCodeDto>() {
                    @Override
                    public InviteCodeDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new InviteCodeDto(rs.getInt("code"),
                                rs.getDate("time_modified"),
                                (0 != rs.getInt("user")));
                    }
                },
                uid
        );
    }

    @Override
    public InviteCode getInviteCode(int code) {
        return template.queryForObject("SELECT uid, code,time_modified, user FROM invite_code WHERE code = ?",
                new RowMapper<InviteCode>() {
                    @Override
                    public InviteCode mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new InviteCode(rs.getInt("uid"),
                                rs.getInt("code"),
                                rs.getInt("user"));
                    }
                },
                code);
    }

    @Override
    public int updateInviteCode(int code, int user) {
        return template.update("UPDATE invite_code SET user = ? WHERE code = ? AND user = 0 ",
                user,
                code);
    }

    @Override
    public boolean isInviteCodeUsed(int code) {
        return 0 == template.queryForObject("SELECT count(id) FROM invite_code WHERE code = ? AND user = 0 ",
                Integer.TYPE,
                code);
    }

}
