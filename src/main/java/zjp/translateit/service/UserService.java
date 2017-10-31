package zjp.translateit.service;

import com.sun.istack.internal.Nullable;
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
import zjp.translateit.data.UserRepository;
import zjp.translateit.domain.User;
import zjp.translateit.util.EncryptUtil;
import zjp.translateit.web.exception.UserExistException;
import zjp.translateit.web.request.LoginRequest;
import zjp.translateit.web.request.RegisterRequest;
import zjp.translateit.web.request.VerifyCodeRequest;

import java.util.concurrent.TimeUnit;

@Service
@PropertySource(value = "classpath:application.properties")
@PropertySource(value = "classpath:application-${spring.profiles.active}.properties")
public class UserService {


    private final UserRepository repository;
    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService;
    private final InviteCodeService inviteCodeService;
    @SuppressWarnings("FieldCanBeLocal")
    private final String EMAIL_KEY_PREFIX = "email:";
    private final String VERIFY_CODE_KEY_PREFIX = "verify.code:";

    @Value("${salt.verify}")
    private String verifySalt;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserService(StringRedisTemplate redisTemplate,
                       UserRepository repository,
                       EmailService emailService,
                       InviteCodeService inviteCodeService) {
        this.redisTemplate = redisTemplate;
        this.repository = repository;
        this.emailService = emailService;
        this.inviteCodeService = inviteCodeService;
    }

    @Nullable
    public User getUserFromLoginRequest(LoginRequest request) {
        User user = repository.findUserByName(request.getName());
        if (user == null)
            return null;
        if (BCrypt.checkpw(request.getPassword(), user.getPassword()))
            return user;
        else
            return null;
    }

    @Transactional
    public void registerUser(RegisterRequest registerRequest) {
        String passwordSalted = BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt(10));
        int uid = repository.generateUid();
        try {
            repository.add(new User(uid, registerRequest.getName(), passwordSalted, registerRequest.getEmail(), User.STATUS.NORMAL));
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            throw new UserExistException();
        }
        inviteCodeService.setInviteCodeUser(registerRequest.getInviteCode(), uid);
        inviteCodeService.addInviteCode(3, uid);
        redisTemplate.delete(VERIFY_CODE_KEY_PREFIX + registerRequest.getEmail());
        redisTemplate.delete(EMAIL_KEY_PREFIX + registerRequest.getEmail());
    }

    public boolean hasUser(String name) {
        return repository.findUserByName(name) != null;
    }

    public boolean emailRegistered(String email) {
        return repository.findUserByEmail(email) != null;
    }

    public boolean checkRequestSign(VerifyCodeRequest request) {
        String raw = request.getEmail() + verifySalt + request.getTimestamp();
        String sign = EncryptUtil.hash(EncryptUtil.Algorithm.MD5, raw);
        return request.getSign().equals(sign);
    }

    public boolean verifyCodeValid(RegisterRequest registerRequest) {
        String verifyCode = redisTemplate.opsForValue().get(VERIFY_CODE_KEY_PREFIX + registerRequest.getEmail());
        return registerRequest.getVerifyCode().equals(verifyCode);
    }

    @Transactional
    public void sendVerifyCode(VerifyCodeRequest request) {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        String verifyCode = generator.generate(9);
        String email = request.getEmail();
        logger.debug("Send email to " + email + " , verifyCode: " + verifyCode);
        emailService.sendVerifyEmail(email, verifyCode);
        redisTemplate.opsForValue().set(VERIFY_CODE_KEY_PREFIX + email, verifyCode, 30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(EMAIL_KEY_PREFIX + email, "", 1, TimeUnit.MINUTES);
    }

    public boolean forbidGetVerifyCode(String email) {
        return redisTemplate.opsForValue().get(EMAIL_KEY_PREFIX + email) != null;
    }

}
