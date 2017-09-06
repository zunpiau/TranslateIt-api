package zjp.translateit.web.response;

import zjp.translateit.domain.Wordbook;

import java.util.List;

class WordbookResponse extends Response {

    private List<Wordbook> wordbooks;

    public WordbookResponse(List<Wordbook> wordbooks) {
        super(ResponseCode.OK);
        this.wordbooks = wordbooks;
    }

    public List<Wordbook> getWordbooks() {
        return wordbooks;
    }
}
