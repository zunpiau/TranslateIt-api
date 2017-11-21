package zjp.translateit.domain;

public class User {

    private long uid;
    private String name;
    private String password;
    private String email;
    private int status;

    public User() {
    }

    public User(String name, String password, String email, int status) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.status = status;
    }

    public User(long uid, String name, String password, String email, int status) {
        this.uid = uid;
        this.name = name;
        this.password = password;
        this.email = email;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public int getStatus() {
        return status;
    }

    public long getUid() {
        return uid;
    }

    public interface Status {

        int NORMAL = 0;
        int DELETE = 1;
    }
}
