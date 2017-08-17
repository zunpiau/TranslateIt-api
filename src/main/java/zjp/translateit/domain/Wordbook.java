package zjp.translateit.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Wordbook {

    @JsonIgnore
    private Long uid;

    private String word;
    private String phEn;
    private String phAm;
    private String phEnUrl;
    private String phAmUrl;
    private String exchange;
    private String means;
    private String sentence;
    private String note;
    private String category;

    public Wordbook() {
    }

    public Wordbook(String word, String phEn, String phAm, String phEnUrl,
                    String phAmUrl, String exchange, String means, String sentence, String note, String category) {
        this.word = word;
        this.phEn = phEn;
        this.phAm = phAm;
        this.phEnUrl = phEnUrl;
        this.phAmUrl = phAmUrl;
        this.exchange = exchange;
        this.means = means;
        this.sentence = sentence;
        this.note = note;
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhEn() {
        return phEn;
    }

    public void setPhEn(String phEn) {
        this.phEn = phEn;
    }

    public String getPhAm() {
        return phAm;
    }

    public void setPhAm(String phAm) {
        this.phAm = phAm;
    }

    public String getPhEnUrl() {
        return phEnUrl;
    }

    public void setPhEnUrl(String phEnUrl) {
        this.phEnUrl = phEnUrl;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getMeans() {
        return means;
    }

    public void setMeans(String means) {
        this.means = means;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhAmUrl() {
        return phAmUrl;
    }

    public void setPhAmUrl(String phAmUrl) {
        this.phAmUrl = phAmUrl;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}