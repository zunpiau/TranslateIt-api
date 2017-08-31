package zjp.translateit.web.Response;

import zjp.translateit.domain.Wordbook;

import java.util.List;

public class WordbookResponse extends Response {

    private List<Wordbook> wordbooks;

    public WordbookResponse(List<Wordbook> wordbooks) {
        super(ResponseCode.OK);
        this.wordbooks = wordbooks;
    }

    public List<Wordbook> getWordbooks() {
        return wordbooks;
    }
}
