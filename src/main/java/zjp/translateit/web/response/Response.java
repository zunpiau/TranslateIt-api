package zjp.translateit.web.response;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;

@JsonSerialize(using = Response.ResponseSerializer.class)
public class Response<T> {

    private ResponseCode code;
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

        BAD_SIGN(300),
        BAD_TOKEN(301),
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
        VERIFY_CODE_USED(407),
        INVITE_CODE_USED(408),
        USER_DELETED(409),

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

    public static class ResponseSerializer extends JsonSerializer<Response> {

        public ResponseSerializer() {
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        }

        @Override
        public void serialize(Response value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeNumberField("code", value.getCode().getStatusCode());
            if (value.getData() != null)
                gen.writeObjectField("data", value.getData());
            gen.writeEndObject();

        }
    }
}
