package zjp.translateit.web.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import zjp.translateit.domain.Token;

class TokenResponse extends Response {

    @JsonSerialize(as = Token.class)
    private Token token;

    public TokenResponse(Token token) {
        super(ResponseCode.OK);
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
