package zjp.translateit.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import zjp.translateit.domain.Token;

import javax.validation.constraints.NotNull;
import java.util.List;

public class RecoverRequest {

    @JsonSerialize(as = Token.class)
    private Token token;
    @NotNull
    @JsonProperty("words")
    private List<String> words;

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
