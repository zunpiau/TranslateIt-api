package zjp.translateit.dto;

import java.util.Date;

public class InviteCodeDto {

    private final int code;
    private final Date usedTime;
    private final boolean used;

    public InviteCodeDto(int code, Date usedTime, boolean used) {
        this.code = code;
        this.usedTime = usedTime;
        this.used = used;
    }

    public int getCode() {
        return code;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public boolean isUsed() {
        return used;
    }
}
