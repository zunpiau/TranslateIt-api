package zjp.translateit.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zjp.translateit.domain.Donation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static zjp.translateit.Constant.CACHE_NAME_DONATION;

@Repository
public class DonationRepository {

    private final JdbcTemplate template;
    private final DonationMapper mapper;

    @Autowired
    public DonationRepository(JdbcTemplate template) {
        this.template = template;
        mapper = new DonationMapper();
    }

    @Cacheable(cacheNames = CACHE_NAME_DONATION)
    public List<Donation> listDonation(int offset) {
        try {
            return template.query("SELECT time, name, amount, comment FROM donation ORDER BY time DESC LIMIT 15 OFFSET ?",
                    mapper,
                    offset);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    private static class DonationMapper implements RowMapper<Donation> {

        @Override
        public Donation mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Donation(resultSet.getTimestamp(1).toLocalDateTime(),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getString(4));
        }
    }

}
