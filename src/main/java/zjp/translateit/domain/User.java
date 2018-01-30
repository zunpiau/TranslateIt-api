package zjp.translateit.domain;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(uid, name, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return uid == user.uid &&
               Objects.equals(name, user.name) &&
               Objects.equals(email, user.email);
    }

    @Override
    public String toString() {
        return "User{" +
               "uid=" + uid +
               ", name='" + name + '\'' +
               ", password='" + password + '\'' +
               ", email='" + email + '\'' +
               ", status=" + status +
               '}';
    }
}
