package zjp.translateit.data;

import zjp.translateit.domain.Wordbook;
import zjp.translateit.dto.ModifyWordbook;

import java.util.List;

public interface WordbookRepository {

    int save(long uid, List<Wordbook> wordbooks);

    List<Wordbook> listNotIn(long uid, List<String> words);

    int update(long uid, List<ModifyWordbook> wordbooks);

    int remove(long uid, List<String> words);

    List<String> listWords(long uid);

}
