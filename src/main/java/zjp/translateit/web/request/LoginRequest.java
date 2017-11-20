package zjp.translateit.web.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginRequest {

    @NotNull
    @Size(min = 3, max = 9)
    @Pattern(regexp = "[a-zA-Z][0-9a-zA-Z_]*")
    private String name;

    @NotNull
    @Size(min = 4, max = 9)
    @Pattern(regexp = "[0-9a-zA-Z]*")
    private String password;

    public LoginRequest(@NotNull @Size(min = 3, max = 9) @Pattern(regexp = "[a-zA-Z][0-9a-zA-Z_]*") String name,
            @NotNull @Size(min = 4, max = 9) @Pattern(regexp = "[0-9a-zA-Z]*") String password) {
        this.name = name;
        this.password = password;
    }

    public LoginRequest() {
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
