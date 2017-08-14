package zjp.translateit.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import zjp.translateit.domain.Wordbook;

import java.util.List;

@Repository
public class WordbookRepositoryImpl implements WordbookRepository {

    private JdbcTemplate template;

    @Autowired
    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public int insert(List<Wordbook> wordbooks) {
        return 0;
    }

    @Override
    public int update(List<Wordbook> wordbooks) {
        return 0;
    }
}
