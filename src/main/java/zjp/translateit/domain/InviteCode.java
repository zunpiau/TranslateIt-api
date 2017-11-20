package zjp.translateit.domain;

public class InviteCode {

    private int uid;
    private int code;
    private int user;

    public InviteCode() {
    }

    public InviteCode(int uid, int code) {
        this.uid = uid;
        this.code = code;
    }

    public InviteCode(int uid, int code, int user) {
        this.uid = uid;
        this.code = code;
        this.user = user;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
