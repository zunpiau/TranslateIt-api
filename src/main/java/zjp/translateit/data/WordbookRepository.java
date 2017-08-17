package zjp.translateit.data;

import zjp.translateit.domain.Wordbook;

import java.util.List;

public interface WordbookRepository {

    void insert(long uid, List<Wordbook> wordbooks);

    void update(long uid, List<Wordbook> wordbooks);

    List<Wordbook> getWordbook(long uid, List<String> words);

    List<Wordbook> getWordbookNotIn(long uid, List<String> words);

    void deleteNotIn(long uid, List<String> words);

    List<String> getWords(long uid);

}
