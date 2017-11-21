package zjp.translateit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class InviteCodeDto {

    private final String code;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Shanghai")
    private final Timestamp usedTime;
    private final boolean used;

    public InviteCodeDto(String code, Timestamp usedTime, boolean used) {
        this.code = code;
        this.usedTime = usedTime;
        this.used = used;
    }

    public String getCode() {
        return code;
    }

    public Timestamp getUsedTime() {
        return usedTime;
    }

    public boolean isUsed() {
        return used;
    }
}
