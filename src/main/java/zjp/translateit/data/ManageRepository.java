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
                    resultSet.getInt("register"),
                    resultSet.getInt("login"),
                    resultSet.getInt("refresh"));
    private final RowMapper<DateTimeCounter> dateTimeCounterRowMapper = (resultSet, i) ->
            new DateTimeCounter(resultSet.getString("date"),
                    resultSet.getString("hour"),
                    resultSet.getInt("count"));

    @Autowired
    public ManageRepository(JdbcTemplate template) {
        this.template = template;
    }

    public List<DateCounter> countDaily(int day) {
        return template.query("SELECT to_char(date_series, 'MM-DD') AS date, register, login, refresh " +
                              "FROM generate_series(current_date - ?, current_date, interval '1 day') AS date_series " +
                              "  LEFT JOIN (select count(id) AS refresh, date_trunc('day', updated_at) AS date FROM token GROUP BY date) t1 " +
                              "    ON date_series = t1.date " +
                              "  LEFT JOIN (select count(id) AS login, date_trunc('day', create_at) AS date FROM token GROUP BY date) t2 " +
                              "    ON date_series = t2.date " +
                              "  LEFT JOIN (select count(id) AS register, date_trunc('day', create_at) AS date FROM account GROUP BY date) t3 " +
                              "    ON date_series = t3.date " +
                              "ORDER BY date_series",
                dateCounterRowMapper,
                day - 1);
    }

    public List<DateTimeCounter> countHourly() {
        return template.query("SELECT to_char(time_series, 'MM-DD') AS date, date_part('hour',time_series) AS hour, count(id) " +
                              " FROM generate_series(date_trunc('hour', now() - interval '23 hours'), now(), interval '1 hour') AS time_series " +
                              " LEFT JOIN token ON time_series = date_trunc('hour', updated_at) " +
                              " GROUP BY time_series " +
                              " ORDER BY time_series",
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
