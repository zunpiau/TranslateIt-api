package zjp.translateit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zjp.translateit.data.WordbookRepository;
import zjp.translateit.domain.Wordbook;

import java.util.ArrayList;
import java.util.List;

@Service
public class WordbookService {

    private final WordbookRepository repository;

    @Autowired
    public WordbookService(WordbookRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void backupWordbook(long uid, List<String> words, List<Wordbook> wordbooksModify) {
        repository.deleteNotIn(uid, words);
        List<String> wordsHaving = repository.getWords(uid);
        ArrayList<Wordbook> wordbooksNew = new ArrayList<>(wordbooksModify.size());
        for (Wordbook wordbook : wordbooksModify) {
            if (!wordsHaving.contains(wordbook.getWord())) {
                wordbooksNew.add(wordbook);
            }
        }
        wordbooksModify.removeAll(wordbooksNew);
        repository.update(uid, wordbooksModify);
        repository.insert(uid, wordbooksNew);
    }

    public List<Wordbook> getWordbooksMissing(long uid, List<String> words) {
        return repository.getWordbookNotIn(uid, words);
    }

}
