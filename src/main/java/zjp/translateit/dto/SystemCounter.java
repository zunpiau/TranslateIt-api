package zjp.translateit.dto;

import java.util.List;

public class SystemCounter {

    private final long user;
    private final long wordbook;
    private final List<DateCounter> refresh;
    private final List<DateCounter> login;
    private final List<DateCounter> register;
    private final List<DateTimeCounter> refreshHourly;

    public SystemCounter(long user,
            long wordbook,
            List<DateCounter> refresh,
            List<DateCounter> login,
            List<DateCounter> register,
            List<DateTimeCounter> refreshHourly) {
        this.user = user;
        this.wordbook = wordbook;
        this.refresh = refresh;
        this.login = login;
        this.register = register;
        this.refreshHourly = refreshHourly;
    }

    public long getUser() {
        return user;
    }

    public List<DateCounter> getRefresh() {
        return refresh;
    }

    public List<DateCounter> getLogin() {
        return login;
    }

    public List<DateCounter> getRegister() {
        return register;
    }

    public List<DateTimeCounter> getRefreshHourly() {
        return refreshHourly;
    }

    public long getWordbook() {
        return wordbook;
    }
}
