package zjp.translateit.web.Response;

import com.fasterxml.jackson.annotation.JsonValue;

public class Response {

    private ResponseCode code;

    public Response(ResponseCode code) {
        this.code = code;
    }

    public ResponseCode getCode() {
        return code;
    }

    public enum ResponseCode {

        OK(200),

        BAD_SIGN(300),
        BAD_TOKEN(301),
        BAD_VERIFY_CODE(302),
        REQUIRE_EXPIRED(303),
        REQUIRE_FRA(304),
        INVALID_ACCOUNT(305),
        TOKEN_EXPIRED(306),
        RE_LOGIN(307),

        INVALID_PARAMETER(400),
        INVALID_EMAIL(401),
        INVALID_PASSWORD(402),
        INVALID_USERNAME(403),
        INVALID_VERIFY_CODE(404),
        EMAIL_REGISTERED(405),
        USERNAME_REGISTERED(406),
        USER_DELETED(407),

        INNER_EXCEPTION(500);

        private int statusCode;

        ResponseCode(int statusCode) {
            this.statusCode = statusCode;
        }

        @JsonValue
        public int getStatusCode() {
            return statusCode;
        }
    }
}
