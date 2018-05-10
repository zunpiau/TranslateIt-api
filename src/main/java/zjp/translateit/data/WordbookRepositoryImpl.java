package zjp.translateit.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import zjp.translateit.domain.Wordbook;
import zjp.translateit.dto.ModifyWordbook;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Repository
public class WordbookRepositoryImpl implements WordbookRepository {

    private final JdbcTemplate template;
    private final String WORDBOOK_FILED = "word, phEn, phAm, phEnUrl, phAmUrl, mean, exchange, sentence, note, category";
    @SuppressWarnings("FieldCanBeLocal")
    private final String SELECT_FROM_WORDBOOK = "SELECT " + WORDBOOK_FILED + " FROM wordbook";
    private final RowMapper<Wordbook> wordbookRowMapper = (rs, rowNum) ->
            new Wordbook(
                    rs.getString("word"),
                    rs.getString("phEn"),
                    rs.getString("phAm"),
                    rs.getString("phEnUrl"),
                    rs.getString("phAmUrl"),
                    rs.getString("mean"),
                    rs.getString("exchange"),
                    rs.getString("sentence"),
                    rs.getString("note"),
                    rs.getString("category")
            );

    @Autowired
    public WordbookRepositoryImpl(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public int save(long uid, List<Wordbook> wordbooks) {
        return template.batchUpdate("INSERT INTO wordbook ( uid, " + WORDBOOK_FILED
                                    + " ) VALUES(?,?,?,?,?,?,?,?,?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Wordbook wordbook = wordbooks.get(i);
                        ps.setLong(1, uid);
                        ps.setString(2, wordbook.getWord());
                        ps.setString(3, wordbook.getPhEn());
                        ps.setString(4, wordbook.getPhAm());
                        ps.setString(5, wordbook.getPhEnUrl());
                        ps.setString(6, wordbook.getPhAmUrl());
                        ps.setString(7, wordbook.getMean());
                        ps.setString(8, wordbook.getExchange());
                        ps.setString(9, wordbook.getSentence());
                        ps.setString(10, wordbook.getNote());
                        ps.setString(11, wordbook.getCategory());
                    }

                    @Override
                    public int getBatchSize() {
                        return wordbooks.size();
                    }
                })
                .length;
    }

    @Override
    public List<Wordbook> listNotIn(long uid, List<String> words) {
        if (words.isEmpty()) {
            return template
                    .query(SELECT_FROM_WORDBOOK + " WHERE uid = ? ",
                            wordbookRowMapper,
                            uid);
        }
        HashMap<String, Object> param = new HashMap<>(3);
        param.put("uid", uid);
        param.put("words", words);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(template);
        return namedParameterJdbcTemplate
                .query(SELECT_FROM_WORDBOOK + " WHERE uid = :uid AND word NOT IN (:words)",
                        param,
                        wordbookRowMapper);
    }

    public int update(long uid, List<ModifyWordbook> wordbooks) {
        return template.batchUpdate("UPDATE wordbook SET note = ?, category = ? WHERE uid = ? AND word = ? ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ModifyWordbook wordbook = wordbooks.get(i);
                        ps.setString(1, wordbook.getNote());
                        ps.setString(2, wordbook.getCategory());
                        ps.setLong(3, uid);
                        ps.setString(4, wordbook.getWord());
                    }

                    @Override
                    public int getBatchSize() {
                        return wordbooks.size();
                    }
                }).length;
    }

    @Override
    public int remove(long uid, List<String> words) {
        HashMap<String, Object> param = new HashMap<>(3);
        param.put("uid", uid);
        param.put("words", words);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(template);
        return namedParameterJdbcTemplate
                .update("DELETE FROM wordbook WHERE uid = :uid AND word IN (:words)",
                        param);
    }

    @Override
    public List<String> listWords(long uid) {
        return template.queryForList("SELECT word FROM wordbook WHERE uid = ? ",
                new Object[]{uid},
                String.class);
    }

}
