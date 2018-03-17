package zjp.translateit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class InviteCodeDto {

    private final String code;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm")
    private final LocalDateTime usedTime;

    public InviteCodeDto(String code, LocalDateTime usedTime) {
        this.code = code;
        this.usedTime = usedTime;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getUsedTime() {
        return usedTime;
    }

}
