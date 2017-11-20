package zjp.translateit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zjp.translateit.data.WordbookRepository;
import zjp.translateit.domain.Wordbook;
import zjp.translateit.dto.BackupResult;

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
    public BackupResult backup(long uid, List<String> words, List<Wordbook> wordbooksModify) {
        BackupResult result = new BackupResult();
        result.setDeleteCount(repository.removeNotIn(uid, words));
        List<String> wordsHaving = repository.listWords(uid);
        Map<Boolean, List<Wordbook>> contains = wordbooksModify.stream()
                .collect(Collectors.partitioningBy(wordbook -> wordsHaving.contains(wordbook.getWord())));
        result.setUpdateCount(repository.updateWordbook(uid, contains.get(true)));
        result.setAddCount(repository.saveWordbook(uid, contains.get(false)));
        return result;
    }

    public List<Wordbook> getWordbooksMissing(long uid, List<String> words) {
        return repository.listWordbookNotIn(uid, words);
    }

}
