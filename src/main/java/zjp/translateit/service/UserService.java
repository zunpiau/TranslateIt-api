package zjp.translateit.service;

import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zjp.translateit.data.TokenRepository;
import zjp.translateit.data.UserRepository;
import zjp.translateit.domain.User;
import zjp.translateit.util.UidGenerator;
import zjp.translateit.web.exception.UserExistException;
import zjp.translateit.web.request.RegisterRequest;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@PropertySource(value = "classpath:application.properties")
public class UserService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService;
    private final InviteCodeService inviteCodeService;
    private final UidGenerator uidGenerator;

    private final String EMAIL_KEY_PREFIX = "email:";
    private final String VERIFY_CODE_KEY_PREFIX = "verify.code:";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RandomStringGenerator generator;
    @Value("${manager.uid}")
    private long managerUid;

    @Autowired
    public UserService(StringRedisTemplate redisTemplate,
            UserRepository repository,
            TokenRepository tokenRepository,
            EmailService emailService,
            InviteCodeService inviteCodeService,
            UidGenerator uidGenerator) {
        this.redisTemplate = redisTemplate;
        this.repository = repository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.inviteCodeService = inviteCodeService;
        this.uidGenerator = uidGenerator;
        generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
    }

    @Nullable
    public User getUser(String account, String password) {
        User user = repository.getUserByAccount(account);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }

    @Transactional
    public void registerUser(RegisterRequest registerRequest) {
        String passwordSalted = BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt(10));
        long uid = uidGenerator.generate();
        try {
            repository.saveUser(new User(uid,
                    registerRequest.getName(),
                    passwordSalted,
                    registerRequest.getEmail(),
                    User.Status.NORMAL));
        } catch (DuplicateKeyException e) {
            logger.debug(e.getMessage());
            throw new UserExistException();
        }
        inviteCodeService.setInviteCodeUser(registerRequest.getInviteCode(), uid);
        inviteCodeService.addInviteCode(3, uid);
        redisTemplate.delete(VERIFY_CODE_KEY_PREFIX + registerRequest.getEmail());
        redisTemplate.delete(EMAIL_KEY_PREFIX + registerRequest.getEmail());
    }

    public boolean emailRegistered(String email) {
        return repository.hasEmail(email);
    }

    public boolean verifyCodeValid(RegisterRequest registerRequest) {
        String verifyCode = redisTemplate
                .opsForValue()
                .get(VERIFY_CODE_KEY_PREFIX + registerRequest.getEmail());
        return registerRequest.getVerifyCode().equals(verifyCode);
    }

    @Transactional
    public void sendVerifyCode(String email) {
        String verifyCode = generator.generate(9);
        logger.debug("Send verify code to [{}]", email);
        List<String> codes = inviteCodeService.addInviteCode(1, managerUid);
        emailService.sendVerifyEmail(email, verifyCode, codes.get(0));
        redisTemplate.opsForValue().set(VERIFY_CODE_KEY_PREFIX + email, verifyCode, 30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(EMAIL_KEY_PREFIX + email, "", 1, TimeUnit.MINUTES);
    }

    public boolean forbidGetVerifyCode(String email) {
        return redisTemplate.hasKey(EMAIL_KEY_PREFIX + email);
    }

    @Transactional
    public int modifyPassword(long uid, String newPassword) {
        tokenRepository.removeAll(uid);
        return repository.modifyPassword(uid, BCrypt.hashpw(newPassword, BCrypt.gensalt(10)));
    }

}
