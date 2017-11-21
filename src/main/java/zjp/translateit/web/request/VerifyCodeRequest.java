package zjp.translateit.web.request;


import javax.validation.constraints.*;

public class VerifyCodeRequest {

    @NotNull
    @Email
    private String email;
    @NotNull
    @Max(1800000000000L)
    @Min(1500000000000L)
    private long timestamp;
    @NotNull
    @Pattern(regexp = "[A-Za-z0-9]{32}")
    private String sign;

    public VerifyCodeRequest() {
    }

    public VerifyCodeRequest(@NotNull @Email String email,
            @NotNull @Max(1800000000000L) @Min(1500000000000L) long timestamp,
            @NotNull @Pattern(regexp = "[A-Za-z0-9]{32}") String sign) {
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
