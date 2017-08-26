package zjp.translateit.web.domain;

import javax.validation.constraints.NotNull;

public class Token {

    private long id;
    private long timestamp;
    @NotNull
    private String sign;

    public Token() {
    }

    public Token(long id, long timestamp, String sign) {
        this.id = id;
        this.timestamp = timestamp;
        this.sign = sign;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
