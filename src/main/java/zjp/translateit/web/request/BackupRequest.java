package zjp.translateit.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import zjp.translateit.domain.Wordbook;

import javax.validation.constraints.NotNull;
import java.util.List;

public class BackupRequest {

    @NotNull
    @JsonProperty("wordbooks")
    @JsonSerialize(as = zjp.translateit.domain.Wordbook.class)
    private List<Wordbook> wordbooks;
    @NotNull
    @JsonProperty("words")
    private List<String> words;

    public BackupRequest(List<Wordbook> wordbooks, List<String> words) {
        this.wordbooks = wordbooks;
        this.words = words;
    }

    public BackupRequest() {
    }

    public List<String> getWords() {
        return words;
    }

    @JsonSetter("words")
    public void setWords(List<String> words) {
        this.words = words;
    }

    public List<Wordbook> getWordbooks() {
        return wordbooks;
    }

    @JsonSetter("wordbooks")
    public void setWordbooks(List<Wordbook> wordbooks) {
        this.wordbooks = wordbooks;
    }
}
