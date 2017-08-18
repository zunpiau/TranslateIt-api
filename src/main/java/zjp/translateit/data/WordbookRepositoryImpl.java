package zjp.translateit.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import zjp.translateit.domain.Wordbook;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Repository
public class WordbookRepositoryImpl implements WordbookRepository {

    private final JdbcTemplate template;

    @Autowired
    public WordbookRepositoryImpl(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void insert(long uid, List<Wordbook> wordbooks) {
        template.batchUpdate("insert into wordbooks " +
                "(uid,word,phEn,phAm,phEnUrl,phAmUrl,means,exchange,sentence,note,category)" +
                "values(?,?,?,?,?,?,?,?,?,?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Wordbook wordbook = wordbooks.get(i);
                ps.setLong(1, uid);
                ps.setString(2, wordbook.getWord());
                ps.setString(3, wordbook.getPhEn());
                ps.setString(4, wordbook.getPhAm());
                ps.setString(5, wordbook.getPhEnUrl());
                ps.setString(6, wordbook.getPhAmUrl());
                ps.setString(7, wordbook.getMeans());
                ps.setString(8, wordbook.getExchange());
                ps.setString(9, wordbook.getSentence());
                ps.setString(10, wordbook.getNote());
                ps.setString(11, wordbook.getCategory());
            }


            @Override
            public int getBatchSize() {
                return wordbooks.size();
            }
        });
    }

    @Override
    public void update(long uid, List<Wordbook> wordbooks) {
        template.batchUpdate("update wordbooks " +
                "set phEn = ?, phAm = ?, phEnUrl = ?, phAmUrl = ?, means = ?, exchange = ?, sentence = ?, note = ?, category = ? " +
                " where uid = ? and word = ? ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Wordbook wordbook = wordbooks.get(i);
                ps.setString(1, wordbook.getPhEn());
                ps.setString(2, wordbook.getPhAm());
                ps.setString(3, wordbook.getPhEnUrl());
                ps.setString(4, wordbook.getPhAmUrl());
                ps.setString(5, wordbook.getMeans());
                ps.setString(6, wordbook.getExchange());
                ps.setString(7, wordbook.getSentence());
                ps.setString(8, wordbook.getNote());
                ps.setString(9, wordbook.getCategory());
                ps.setLong(10, uid);
                ps.setString(11, wordbook.getWord());
            }

            @Override
            public int getBatchSize() {
                return wordbooks.size();
            }
        });
    }

    @Override
    public List<Wordbook> getWordbook(long uid, List<String> words) {
        HashMap<String, Object> param = new HashMap<>(3);
        param.put("uid", uid);
        param.put("words", words);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(template);
        return namedParameterJdbcTemplate.query("select word,phEn,phAm,phEnUrl,phAmUrl,means,exchange,sentence,note, category " +
                        " from wordbooks where uid = :uid and word in (:words)",
                param,
                new WordbookRowMapper());
    }

    @Override
    public List<Wordbook> getWordbookNotIn(long uid, List<String> words) {
        if (words.isEmpty()) {
            return template.query("select word,phEn,phAm,phEnUrl,phAmUrl,means,exchange,sentence,note, category " +
                            "from wordbooks where uid = ? ",
                    new WordbookRowMapper(),
                    uid);
        }
        HashMap<String, Object> param = new HashMap<>(3);
        param.put("uid", uid);
        param.put("words", words);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(template);
        return namedParameterJdbcTemplate.query("select word,phEn,phAm,phEnUrl,phAmUrl,means,exchange,sentence,note, category " +
                        "from wordbooks where uid = :uid and word not in (:words)",
                param,
                new WordbookRowMapper());
    }

    public void deleteNotIn(long uid, List<String> words) {
        if (words.isEmpty()) {
            template.update("delete from wordbooks where uid = ? ", uid);
        } else {
            HashMap<String, Object> param = new HashMap<>(3);
            param.put("uid", uid);
            param.put("words", words);
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(template);
            namedParameterJdbcTemplate.update("delete from wordbooks where uid = :uid and word not in (:words)", param);
        }
    }

    public List<String> getWords(long uid) {
        return template.queryForList("select word from wordbooks where uid = ? ", new Object[]{uid}, String.class);
    }

    static class WordbookRowMapper implements RowMapper<Wordbook> {

        @Override
        public Wordbook mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Wordbook(
                    rs.getString("word"),
                    rs.getString("phEn"),
                    rs.getString("phAm"),
                    rs.getString("phEnUrl"),
                    rs.getString("phAmUrl"),
                    rs.getString("means"),
                    rs.getString("exchange"),
                    rs.getString("sentence"),
                    rs.getString("note"),
                    rs.getString("category")
            );
        }
    }
}
