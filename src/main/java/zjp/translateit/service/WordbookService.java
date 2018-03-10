package zjp.translateit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zjp.translateit.data.WordbookRepository;
import zjp.translateit.domain.Wordbook;
import zjp.translateit.dto.ModifyWordbook;

import java.util.List;

@Service
public class WordbookService {

    private final WordbookRepository repository;

    @Autowired
    public WordbookService(WordbookRepository repository) {
        this.repository = repository;
    }

    public int deleteWordbook(long uid, List<String> words) {
        if (words.size() == 0) {
            return 0;
        }
        return repository.remove(uid, words);
    }

    public int updateWordbook(long uid, List<ModifyWordbook> wordbooks) {
        if (wordbooks.size() == 0) {
            return 0;
        }
        return repository.update(uid, wordbooks);
    }

    public int addWordbook(long uid, List<Wordbook> wordbooks) {
        if (wordbooks.size() == 0) {
            return 0;
        }
        return repository.save(uid, wordbooks);
    }

    public List<String> getWords(long uid) {
        return repository.listWords(uid);
    }

    public List<Wordbook> getWordbooksMissing(long uid, List<String> words) {
        return repository.listNotIn(uid, words);
    }

}
