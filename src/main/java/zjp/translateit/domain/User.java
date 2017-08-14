package zjp.translateit.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class User {

    private Long id;

    @NotNull
    @Size(min = 3, max = 9)
    @Pattern(regexp = "[0-9a-zA-Z]*")
    private String name;

    @NotNull
    @Size(min = 6, max = 9)
    @Pattern(regexp = "[0-9a-zA-Z]*")
    private String password;

    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9+._%+-]+@[a-zA-Z0-9]+[.][a-zA-Z0-9]+")
    private String email;

    private int status;

    public User() {
    }

    public User(@NotNull @Size(min = 3, max = 9) @Pattern(regexp = "[0-9a-zA-Z]*") String name,
                @NotNull @Size(min = 6, max = 9) @Pattern(regexp = "[0-9a-zA-Z]*") String password,
                @NotNull String email,
                int status) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.status = status;
    }

    public User(@NotNull @Size(min = 3, max = 9) @Pattern(regexp = "[0-9a-zA-Z]*") String name,
                @NotNull @Size(min = 6, max = 9) @Pattern(regexp = "[0-9a-zA-Z]*") String password,
                @NotNull String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public User(Long id,
                @NotNull @Size(min = 3, max = 9) @Pattern(regexp = "[0-9a-zA-Z]*") String name,
                @NotNull @Size(min = 6, max = 9) @Pattern(regexp = "[0-9a-zA-Z]*") String password,
                @NotNull String email,
                int status) {
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
