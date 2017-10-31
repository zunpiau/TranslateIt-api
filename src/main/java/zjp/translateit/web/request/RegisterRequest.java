package zjp.translateit.web.request;

import javax.validation.constraints.*;

public class RegisterRequest {

    @NotNull
    @Size(min = 3, max = 9)
    @Pattern(regexp = "[a-zA-Z][0-9a-zA-Z_]*")
    private String name;

    @NotNull
    @Size(min = 4, max = 9)
    @Pattern(regexp = "[0-9a-zA-Z]*")
    private String password;

    @NotNull
    @Pattern(regexp = "[a-z]{9}")
    private String verifyCode;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Max(90000000)
    @Min(10000000)
    private int inviteCode;

    public RegisterRequest() {
    }

    public RegisterRequest(@NotNull @Size(min = 3, max = 9) @Pattern(regexp = "[a-zA-Z][0-9a-zA-Z_]*") String name,
                           @NotNull @Size(min = 4, max = 9) @Pattern(regexp = "[0-9a-zA-Z]*") String password,
                           @NotNull @Pattern(regexp = "[a-z]{9}") String verifyCode,
                           @NotNull @Email String email,
                           @NotNull @Max(90000000) @Min(10000000) int inviteCode) {
        this.name = name;
        this.password = password;
        this.verifyCode = verifyCode;
        this.email = email;
        this.inviteCode = inviteCode;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public int getInviteCode() {
        return inviteCode;
    }
}
