package zjp.translateit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import zjp.translateit.data.ManageRepository;
import zjp.translateit.domain.Token;
import zjp.translateit.dto.SystemCounter;
import zjp.translateit.util.EncryptUtil;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

@Service
@PropertySource(value = "classpath:application.properties")
public class ManageService {

    private final ManageRepository repository;
    private final InviteCodeService inviteCodeService;
    @Value("${manager.uid}")
    private long rootUid;
    @Value("${salt.token.manager}")
    private String tokenSalt;
    @Value("${salt.account.manager}")
    private String accountSalt;
    @Value("${manager.name}")
    private String rootName;
    @Value("${manager.password.hashed}")
    private String passwordHashed;

    @Autowired
    public ManageService(ManageRepository repository, InviteCodeService inviteCodeService) {
        this.repository = repository;
        this.inviteCodeService = inviteCodeService;
    }


    public List<String> addManagerInviteCode(int amount) {
        return inviteCodeService.addInviteCode(amount, rootUid);
    }

    public SystemCounter getSystemCounter() {
        return new SystemCounter(
                repository.countUser(),
                repository.countInviteCode(rootUid),
                repository.countWordbook(),
                repository.countRefreshDaily(7),
                repository.countLoginDaily(7),
                repository.countRegisterDaily(7),
                repository.countRefreshHourly()
        );
    }

    @Nullable
    public String login(String name, String password) {
        if (name.equals(rootName)
            && passwordHashed.equals(EncryptUtil.hash(EncryptUtil.Algorithm.SHA256,
                name + accountSalt + password))) {
            Token token = generateToken(rootUid);
            return Base64.getEncoder()
                    .encodeToString((token.getUid() + "." + token.getTimestamp() + "." + token.getSign()).getBytes());
        } else {
            return null;
        }
    }

    private Token generateToken(long uid) {
        long currentTime = Instant.now().getEpochSecond();
        String key = EncryptUtil.hash(EncryptUtil.Algorithm.SHA256, uid + tokenSalt + currentTime);
        return new Token(uid, currentTime, key);
    }
}
