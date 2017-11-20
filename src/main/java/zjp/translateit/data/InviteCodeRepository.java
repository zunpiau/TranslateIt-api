package zjp.translateit.data;

import zjp.translateit.domain.InviteCode;
import zjp.translateit.dto.InviteCodeDto;

import java.util.List;

public interface InviteCodeRepository {

    int generateCode();

    void saveInviteCode(List<InviteCode> inviteCodes);

    List<InviteCodeDto> listInviteCode(int uid);

    InviteCode getInviteCode(int code);

    int updateInviteCode(int code, int user);

    boolean isInviteCodeUsed(int code);

}
