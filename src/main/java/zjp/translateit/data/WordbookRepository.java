package zjp.translateit.data;

import zjp.translateit.domain.Wordbook;

import java.util.List;

public interface WordbookRepository {

    int saveWordbook(long uid, List<Wordbook> wordbooks);

    int updateWordbook(long uid, List<Wordbook> wordbooks);

    List<Wordbook> listWordbook(long uid, List<String> words);

    List<Wordbook> listWordbookNotIn(long uid, List<String> words);

    int removeNotIn(long uid, List<String> words);

    List<String> listWords(long uid);

}
