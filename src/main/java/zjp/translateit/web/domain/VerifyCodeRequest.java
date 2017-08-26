package zjp.translateit.web.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class VerifyCodeRequest {

    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9+._%+-]+@[a-zA-Z0-9.]+")
    private String email;
    private long timestamp;
    @NotNull
    private String sign;

    public VerifyCodeRequest() {
    }

    public VerifyCodeRequest(@NotNull @Pattern(regexp = "[a-zA-Z0-9+._%+-]+@[a-zA-Z0-9.]+") String email,
                             long timestamp,
                             @NotEmpty String sign) {
        this.email = email;
        this.timestamp = timestamp;
        this.sign = sign;
    }

    public String getEmail() {
        return email;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSign() {
        return sign;
    }
}
