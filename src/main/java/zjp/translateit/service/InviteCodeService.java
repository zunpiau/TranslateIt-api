package zjp.translateit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zjp.translateit.data.InviteCodeRepository;
import zjp.translateit.domain.InviteCode;
import zjp.translateit.dto.InviteCodeDto;
import zjp.translateit.util.InviteCodeGenerator;
import zjp.translateit.web.exception.InviteCodeUsedException;

import java.util.ArrayList;
import java.util.List;

@Service
public class InviteCodeService {

    private final InviteCodeRepository repository;
    private final InviteCodeGenerator inviteCodeGenerator;

    @Autowired
    public InviteCodeService(InviteCodeRepository repository, InviteCodeGenerator inviteCodeGenerator) {
        this.repository = repository;
        this.inviteCodeGenerator = inviteCodeGenerator;
    }

    @Transactional
    public List<InviteCode> addInviteCode(int count, long uid) {
        List<InviteCode> codes = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            codes.add(new InviteCode(uid, inviteCodeGenerator.generate()));
        }
        repository.saveInviteCode(codes);
        return codes;
    }

    public List<InviteCodeDto> getInviteCode(long uid) {
        return repository.listInviteCode(uid);
    }

    public void setInviteCodeUser(String code, long uid) {
        if (repository.updateInviteCode(code, uid) != 1) {
            throw new InviteCodeUsedException();
        }
    }

}
