package zjp.translateit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zjp.translateit.data.WordbookRepository;
import zjp.translateit.domain.Wordbook;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        Map<Boolean, List<Wordbook>> contains = wordbooksModify.stream()
                .collect(Collectors.partitioningBy(wordbook -> wordsHaving.contains(wordbook.getWord())));
        repository.update(uid, contains.get(true));
        repository.insert(uid, contains.get(false));
    }

    public List<Wordbook> getWordbooksMissing(long uid, List<String> words) {
        return repository.getWordbookNotIn(uid, words);
    }

}
