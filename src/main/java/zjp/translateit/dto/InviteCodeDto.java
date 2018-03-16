package zjp.translateit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class InviteCodeDto {

    private final String code;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm", timezone = "Asia/Shanghai")
    private final Timestamp usedTime;

    public InviteCodeDto(String code, Timestamp usedTime) {
        this.code = code;
        this.usedTime = usedTime;
    }

    public String getCode() {
        return code;
    }

    public Timestamp getUsedTime() {
        return usedTime;
    }

}
