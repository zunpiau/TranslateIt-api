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
    public void saveInviteCode(List<InviteCode> inviteCodes) {
        template.batchUpdate("INSERT INTO invite_code (uid, code) VALUES (?, ? )",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, inviteCodes.get(i).getUid());
                        ps.setString(2, inviteCodes.get(i).getCode());
                    }

                    @Override
                    public int getBatchSize() {
                        return inviteCodes.size();
                    }
                });
    }

    @Override
    public List<InviteCodeDto> listInviteCode(long uid) {
        return template.query("SELECT code, updated_at, user_id FROM invite_code WHERE uid = ? ",
                new RowMapper<InviteCodeDto>() {
                    @Override
                    public InviteCodeDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        boolean used = 0 != rs.getLong("user_id");
                        return new InviteCodeDto(rs.getString("code"),
                                used ? rs.getTimestamp("updated_at") : null,
                                used);
                    }
                },
                uid
        );
    }

    @Override
    public InviteCode getInviteCode(String code) {
        return template.queryForObject("SELECT uid, code,updated_at, user_id FROM invite_code WHERE code = ?",
                new RowMapper<InviteCode>() {
                    @Override
                    public InviteCode mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new InviteCode(rs.getInt("uid"),
                                rs.getString("code"),
                                rs.getInt("user_id"));
                    }
                },
                code);
    }

    @Override
    public int updateInviteCode(String code, long userId) {
        return template.update("UPDATE invite_code SET user_id = ? WHERE code = ? AND user_id = 0 ",
                userId,
                code);
    }

}
