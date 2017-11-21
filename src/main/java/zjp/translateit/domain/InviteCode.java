package zjp.translateit.domain;

public class InviteCode {

    private long uid;
    private String code;
    private int user;

    public InviteCode() {
    }

    public InviteCode(long uid, String code) {
        this.uid = uid;
        this.code = code;
    }

    public InviteCode(long uid, String code, int user) {
        this.uid = uid;
        this.code = code;
        this.user = user;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
