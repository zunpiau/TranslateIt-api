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
    private String mean;
    private String exchange;
    private String sentence;
    private String note;
    private String category;

    public Wordbook() {
    }

    public Wordbook(String word, String phEn, String phAm, String phEnUrl,
            String phAmUrl, String mean, String exchange, String sentence, String note, String category) {
        this.word = word;
        this.phEn = phEn;
        this.phAm = phAm;
        this.phEnUrl = phEnUrl;
        this.phAmUrl = phAmUrl;
        this.mean = mean;
        this.exchange = exchange;
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

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
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