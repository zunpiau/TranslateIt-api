package zjp.translateit.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import zjp.translateit.domain.Wordbook;

import java.util.List;

public class BackupRequest {

    @JsonSerialize(as = Token.class)
    private Token token;
    @JsonProperty("list")
    @JsonSerialize(as = zjp.translateit.domain.Wordbook.class)
    private List<Wordbook> wordbooks;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public List<Wordbook> getWordbooks() {
        return wordbooks;
    }

    public void setWordbooks(List<Wordbook> wordbooks) {
        this.wordbooks = wordbooks;
    }

}
