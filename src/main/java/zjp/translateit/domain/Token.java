package zjp.translateit.domain;

import javax.validation.constraints.NotNull;

public class Token {

    @NotNull
    private long uid;
    @NotNull
    private long timestamp;
    @NotNull
    private String sign;

    public Token() {
    }

    public Token(long uid, long timestamp, String sign) {
        this.uid = uid;
        this.timestamp = timestamp;
        this.sign = sign;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
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
