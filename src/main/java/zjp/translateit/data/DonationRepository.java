package zjp.translateit.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zjp.translateit.domain.Donation;

import java.util.List;

import static zjp.translateit.Constant.CACHE_NAME_DONATION;

@Repository
public class DonationRepository {

    private final JdbcTemplate template;
    private final RowMapper<Donation> donationRowMapper = (resultSet, i) ->
            new Donation(resultSet.getTimestamp(1).toLocalDateTime(),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getString(4));

    @Autowired
    public DonationRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Cacheable(cacheNames = CACHE_NAME_DONATION, unless = "#result == null || #result.size() == 0")
    public List<Donation> listDonation(int offset) {
        return template.query("SELECT time, name, amount, comment FROM donation ORDER BY time DESC LIMIT 15 OFFSET ?",
                donationRowMapper,
                offset);
    }

}
