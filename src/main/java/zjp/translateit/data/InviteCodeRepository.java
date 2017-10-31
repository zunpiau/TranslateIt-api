package zjp.translateit.data;

import zjp.translateit.domain.InviteCode;
import zjp.translateit.dto.InviteCodeDto;

import java.util.List;

public interface InviteCodeRepository {

    int generateCode();

    void batchAdd(List<InviteCode> inviteCodes);

    List<InviteCodeDto> getInviteCode(int uid);

    InviteCode findInviteCode(int code);

    int setInviteCodeUser(int code, int user);

    boolean isInviteCodeUsed(int code);

}
