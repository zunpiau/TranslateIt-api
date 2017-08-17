package zjp.translateit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public void deleteWordbooks(long uid, List<String> words) {
        repository.deleteNotIn(uid, words);
    }

    public void insertOrUpdate(long uid, List<Wordbook> wordbooksAll) {
        List<String> wordsHaving = repository.getWords(uid);
        System.out.println(wordbooksAll.size());
        ArrayList<Wordbook> wordbooksNew = new ArrayList<>(wordbooksAll.size());
        for (Wordbook wordbook : wordbooksAll) {
            if (!wordsHaving.contains(wordbook.getWord())) {
                wordbooksNew.add(wordbook);
            }
        }
        wordbooksAll.removeAll(wordbooksNew);
        repository.update(uid, wordbooksAll);
        repository.insert(uid, wordbooksNew);
    }

    public List<Wordbook> getWordbooksMissing(long uid, List<String> words) {
        return repository.getWordbookNotIn(uid, words);
    }

}
