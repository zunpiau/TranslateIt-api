package zjp.translateit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zjp.translateit.data.InviteCodeRepository;
import zjp.translateit.domain.InviteCode;
import zjp.translateit.dto.InviteCodeDto;
import zjp.translateit.web.exception.InviteCodeUsedException;

import java.util.ArrayList;
import java.util.List;

@Service
public class InviteCodeService {

    private final InviteCodeRepository repository;

    @Autowired
    public InviteCodeService(InviteCodeRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void addInviteCode(int count, int uid) {
        ArrayList<InviteCode> codes = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            codes.add(new InviteCode(uid, repository.generateCode()));
        }
        repository.saveInviteCode(codes);
    }

    public boolean isInviteCodeUsed(int code) {
        return repository.isInviteCodeUsed(code);
    }

    public List<InviteCodeDto> getInviteCode(int uid) {
        return repository.listInviteCode(uid);
    }

    public void setInviteCodeUser(int code, int uid) {
        if (repository.updateInviteCode(code, uid) != 1) {
            throw new InviteCodeUsedException();
        }
    }

}
