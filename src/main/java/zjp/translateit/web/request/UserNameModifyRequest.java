package zjp.translateit.web.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserNameModifyRequest {

    @NotNull
    private String account;

    @NotNull
    @Size(min = 4, max = 9)
    @Pattern(regexp = "[0-9a-zA-Z]*")
    private String password;

    @NotNull
    @Size(min = 3, max = 9)
    @Pattern(regexp = "[a-zA-Z][0-9a-zA-Z_]*")
    private String newUserName;

    public UserNameModifyRequest() {
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewUserName() {
        return newUserName;
    }

    public void setNewUserName(String newUserName) {
        this.newUserName = newUserName;
    }
}
