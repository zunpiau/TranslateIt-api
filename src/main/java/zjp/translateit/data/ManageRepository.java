package zjp.translateit.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zjp.translateit.domain.Donation;
import zjp.translateit.dto.DateCounter;
import zjp.translateit.dto.DateTimeCounter;

import java.util.List;

import static zjp.translateit.Constant.CACHE_NAME_DONATION;

@SuppressWarnings("ConstantConditions")
@Repository
public class ManageRepository {

    private final JdbcTemplate template;
    private final RowMapper<DateCounter> dateCounterRowMapper = (resultSet, i) ->
            new DateCounter(resultSet.getString("date"),
                    resultSet.getInt("count"));
    private final RowMapper<DateTimeCounter> dateTimeCounterRowMapper = (resultSet, i) ->
            new DateTimeCounter(resultSet.getString("date"),
                    resultSet.getString("hour"),
                    resultSet.getInt("count"));

    @Autowired
    public ManageRepository(JdbcTemplate template) {
        this.template = template;
    }

    public List<DateCounter> countLoginDaily(int day) {
        return template.query("SELECT to_char(create_at ,'MM-DD') AS date, COUNT(id) " +
                              "FROM token WHERE create_at > current_date - ? " +
                              "GROUP BY date",
                dateCounterRowMapper,
                day - 1);
    }

    public List<DateCounter> countRegisterDaily(int day) {
        return template.query("SELECT to_char(create_at, 'MM-DD') AS date, COUNT(id) " +
                              "FROM account WHERE create_at > current_date - ? " +
                              "GROUP BY date",
                dateCounterRowMapper,
                day - 1);
    }

    public List<DateCounter> countRefreshDaily(int day) {
        return template.query("SELECT to_char(updated_at, 'MM-DD') AS date, COUNT(id) " +
                              "FROM token WHERE updated_at > current_date - ? " +
                              "GROUP BY date",
                dateCounterRowMapper,
                day - 1);
    }

    public List<DateTimeCounter> countRefreshHourly() {
        return template.query("SELECT to_char(updated_at, 'MM-DD') AS date, " +
                              "EXTRACT(HOUR FROM updated_at) AS hour, " +
                              "COUNT(id) " +
                              "FROM token WHERE updated_at > (now() - interval '24 hours') " +
                              "GROUP BY date, hour",
                dateTimeCounterRowMapper);
    }

    public long countUser() {
        return template.queryForObject("SELECT COUNT(*) FROM account", long.class);
    }

    public long countWordbook() {
        return template.queryForObject("SELECT COUNT(*) FROM wordbook", long.class);
    }

    @CacheEvict(cacheNames = CACHE_NAME_DONATION, allEntries = true)
    public int saveDonation(Donation donation) {
        return template.update("INSERT INTO donation(trade, time, name, amount, comment) VALUES(?, ?, ?, ?, ?)",
                donation.getTrade(),
                donation.getTime(),
                donation.getName(),
                donation.getAmount(),
                donation.getComment());
    }

}
