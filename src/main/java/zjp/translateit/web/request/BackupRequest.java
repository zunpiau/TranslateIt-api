package zjp.translateit.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import zjp.translateit.domain.Token;
import zjp.translateit.domain.Wordbook;

import java.util.List;

public class BackupRequest {

    @JsonSerialize(as = Token.class)
    private Token token;
    @JsonProperty("wordbooks")
    @JsonSerialize(as = zjp.translateit.domain.Wordbook.class)
    private List<Wordbook> wordbooks;
    @JsonProperty("words")
    private List<String> words;

    public BackupRequest(Token token, List<Wordbook> wordbooks, List<String> words) {
        this.token = token;
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

    public Token getToken() {
        return token;
    }

    @JsonSetter("token")
    public void setToken(Token token) {
        this.token = token;
    }

    public List<Wordbook> getWordbooks() {
        return wordbooks;
    }

    @JsonSetter("wordbooks")
    public void setWordbooks(List<Wordbook> wordbooks) {
        this.wordbooks = wordbooks;
    }
}
