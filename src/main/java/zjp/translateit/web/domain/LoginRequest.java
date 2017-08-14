package zjp.translateit.web.domain;

public class LoginRequest {

    private String name;

    private String password;

    public LoginRequest(String name, String password) {
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
