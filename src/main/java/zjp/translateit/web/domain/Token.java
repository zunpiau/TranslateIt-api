package zjp.translateit.web.domain;

import javax.validation.constraints.NotNull;

public class Token {

    private long id;
    private long timestamp;
    @NotNull
    private String key;

    public Token() {
    }

    public Token(long id, long timestamp, String key) {
        this.id = id;
        this.timestamp = timestamp;
        this.key = key;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
