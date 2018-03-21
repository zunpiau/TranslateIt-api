package zjp.translateit.data;

import zjp.translateit.domain.InviteCode;
import zjp.translateit.dto.InviteCodeDto;

import java.util.List;

public interface InviteCodeRepository {

    void saveInviteCode(List<InviteCode> inviteCodes);

    void saveInviteCode(List<String> inviteCodes, long uid);

    List<InviteCodeDto> listInviteCode(long uid);

    InviteCode getInviteCode(String code);

    int updateInviteCode(String code, long user);

}
