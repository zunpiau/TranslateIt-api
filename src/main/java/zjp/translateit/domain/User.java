package zjp.translateit.domain;

public class User {

    private Long id;

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

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public User(Long id, String name, String password, String email, int status) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.status = status;
    }

    public static User setUserId(long id, User user) {
        return new User(id, user.getName(), user.getPassword(), user.getEmail(), user.getStatus());
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

    public Long getId() {
        return id;
    }

    public interface STATUS {

        int NORMAL = 0;
        int DELETE = 1;
    }
}
