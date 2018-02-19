package zjp.translateit.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;

public class Response<T> {

    private final ResponseCode code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Response(ResponseCode code) {
        this.code = code;
    }

    public Response(T data) {
        code = ResponseCode.OK;
        this.data = data;
    }

    public ResponseCode getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public enum ResponseCode {

        OK(200),

        REQUIRE_FRA(304),
        INVALID_ACCOUNT(305),
        RE_LOGIN(307),

        INVALID_PARAMETER(400),
        EMAIL_REGISTERED(405),
        USERNAME_REGISTERED(406),
        VERIFY_CODE_USED(407),
        INVITE_CODE_USED(408),
        USER_DELETED(409),

        INNER_EXCEPTION(500),
        EMAIL_SEND_FAILED(501);

        private final int statusCode;

        ResponseCode(int statusCode) {
            this.statusCode = statusCode;
        }

        @JsonValue
        public int getStatusCode() {
            return statusCode;
        }
    }
}
