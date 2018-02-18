package zjp.translateit.web.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginRequest {

    @NotNull
    private String account;

    @NotNull
    @Size(min = 4, max = 9)
    @Pattern(regexp = "[0-9a-zA-Z]*")
    private String password;

    public LoginRequest(@NotNull String account,
            @NotNull @Size(min = 4, max = 9) @Pattern(regexp = "[0-9a-zA-Z]*") String password) {
        this.account = account;
        this.password = password;
    }

    public LoginRequest() {
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }
}
