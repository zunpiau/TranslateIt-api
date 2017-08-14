package zjp.translateit.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

public class RecoverRequest {

    @JsonSerialize(as = Token.class)
    private Token token;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    public Date getDate() {
        return date;
    }

    public Token getToken() {
        return token;
    }
}
