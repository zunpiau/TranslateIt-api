package zjp.translateit.dto;

public class DateCounter {

    private final String date;
    private final int register;
    private final int login;
    private final int refresh;

    public DateCounter(String date, int register, int login, int refresh) {
        this.date = date;
        this.register = register;
        this.login = login;
        this.refresh = refresh;
    }

    public String getDate() {
        return date;
    }

    public int getRegister() {
        return register;
    }

    public int getLogin() {
        return login;
    }

    public int getRefresh() {
        return refresh;
    }
}
