package zjp.translateit.web.request;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
    @Length(max = 8, min = 8)
    private String inviteCode;

    public RegisterRequest() {
    }

    public RegisterRequest(@NotNull @Size(min = 3, max = 9) @Pattern(regexp = "[a-zA-Z][0-9a-zA-Z_]*") String name,
            @NotNull @Size(min = 4, max = 9) @Pattern(regexp = "[0-9a-zA-Z]*") String password,
            @NotNull @Pattern(regexp = "[a-z]{9}") String verifyCode,
            @NotNull @Email String email,
            @NotNull @Length(max = 8, min = 8) String inviteCode) {
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

    public String getInviteCode() {
        return inviteCode;
    }
}
