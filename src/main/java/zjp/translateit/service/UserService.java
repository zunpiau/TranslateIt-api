package zjp.translateit.service;

import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zjp.translateit.data.UserRepository;
import zjp.translateit.domain.User;
import zjp.translateit.util.UidGenerator;
import zjp.translateit.web.exception.UserExistException;
import zjp.translateit.web.request.LoginRequest;
import zjp.translateit.web.request.RegisterRequest;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

@Service
@PropertySource(value = "classpath:application.properties")
public class UserService {

    private final UserRepository repository;
    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService;
    private final InviteCodeService inviteCodeService;
    private final UidGenerator uidGenerator;

    private final String EMAIL_KEY_PREFIX = "email:";
    private final String VERIFY_CODE_KEY_PREFIX = "verify.code:";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RandomStringGenerator generator;

    @Autowired
    public UserService(StringRedisTemplate redisTemplate,
            UserRepository repository,
            EmailService emailService,
            InviteCodeService inviteCodeService,
            UidGenerator uidGenerator) {
        this.redisTemplate = redisTemplate;
        this.repository = repository;
        this.emailService = emailService;
        this.inviteCodeService = inviteCodeService;
        this.uidGenerator = uidGenerator;
        generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
    }

    @Nullable
    public User getUserFromLoginRequest(LoginRequest request) {
        User user = repository.getUserByAccount(request.getAccount());
        if (user != null && BCrypt.checkpw(request.getPassword(), user.getPassword())) {
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
        emailService.sendVerifyEmail(email, verifyCode);
        redisTemplate.opsForValue().set(VERIFY_CODE_KEY_PREFIX + email, verifyCode, 30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(EMAIL_KEY_PREFIX + email, "", 1, TimeUnit.MINUTES);
    }

    public boolean forbidGetVerifyCode(String email) {
        return redisTemplate.hasKey(EMAIL_KEY_PREFIX + email);
    }

}
