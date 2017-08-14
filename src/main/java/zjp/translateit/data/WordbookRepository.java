package zjp.translateit.data;

import zjp.translateit.domain.Wordbook;

import java.util.List;

public interface WordbookRepository {

    public int insert(List<Wordbook> wordbooks);

    public int update(List<Wordbook> wordbooks);

}
