package zjp.translateit.domain;

public class Wordbook {

    private Long id;
    private Long uid;
    private String wordName;
    private String phEn;
    private String phAm;
    private String phEnUrl;
    private String phAmUrl;
    private String exchange;
    private String means;
    private String sentence;
    private String note;

    public Wordbook() {
    }

    public Wordbook(Long uid, String wordName, String phEn, String phAm, String phEnUrl,
                    String phAmUrl, String exchange, String means, String sentence, String note) {
        this.uid = uid;
        this.wordName = wordName;
        this.phEn = phEn;
        this.phAm = phAm;
        this.phEnUrl = phEnUrl;
        this.phAmUrl = phAmUrl;
        this.exchange = exchange;
        this.means = means;
        this.sentence = sentence;
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

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
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

}