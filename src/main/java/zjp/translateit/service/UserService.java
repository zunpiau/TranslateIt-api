package zjp.translateit.service;

import com.aliyuncs.exceptions.ClientException;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zjp.translateit.data.UserRepository;
import zjp.translateit.domain.User;
import zjp.translateit.util.EncryptUtil;
import zjp.translateit.web.request.LoginRequest;
import zjp.translateit.web.request.RegisterRequest;
import zjp.translateit.web.request.VerifyCodeRequest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@PropertySource(value = "classpath:application.properties")
@PropertySource(value = "classpath:application-${spring.profiles.active}.properties")
public class UserService {


    private final UserRepository repository;
    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService;
    @SuppressWarnings("FieldCanBeLocal")
    private final String EMAIL_KEY_PREFIX = "email:";
    private final String VERIFY_CODE_KEY_PREFIX = "verify.code:";

    @Value("${salt.verify}")
    private String verifySalt;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserService(StringRedisTemplate redisTemplate, UserRepository repository, EmailService emailService) {
        this.redisTemplate = redisTemplate;
        this.repository = repository;
        this.emailService = emailService;
    }

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
    public int registerUser(RegisterRequest registerRequest) {
        String passwordSalted = BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt(10));
        int uid = repository.generateUid();
        repository.add(new User(uid, registerRequest.getName(), passwordSalted, registerRequest.getEmail(), User.STATUS.NORMAL));
        redisTemplate.delete(VERIFY_CODE_KEY_PREFIX + registerRequest.getEmail());
        redisTemplate.delete(EMAIL_KEY_PREFIX + registerRequest.getEmail());
        return uid;
    }

    public boolean hasUser(String name) {
        return repository.findUserByName(name) != null;
    }

    public boolean emailRegistered(String email) {
        return repository.findUserByEmail(email) != null;
    }

    public boolean checkVerifyCodeSign(VerifyCodeRequest request) {
        String raw = request.getEmail() + verifySalt + request.getTimestamp();
        String sign = EncryptUtil.hash(EncryptUtil.Algorithm.MD5, raw);
        return request.getSign().equals(sign);
    }

    public boolean checkVerifyCode(RegisterRequest registerRequest) {
        String verifyCode = redisTemplate.opsForValue().get(VERIFY_CODE_KEY_PREFIX + registerRequest.getEmail());
        return registerRequest.getVerifyCode().equals(verifyCode);
    }

    @Transactional
    public void sendVerifyCode(VerifyCodeRequest request) throws IOException, ClientException {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        String verifyCode = generator.generate(9);
        String email = request.getEmail();
        logger.debug("Send email to " + email + " , verifyCode: " + verifyCode);
        if (!emailService.sendVerifyEmail(email, verifyCode))
            throw new ClientException("");
        redisTemplate.opsForValue().set(VERIFY_CODE_KEY_PREFIX + email, verifyCode, 30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(EMAIL_KEY_PREFIX + email, "", 1, TimeUnit.MINUTES);
    }

    public boolean forbidGetVerifyCode(String email) {
        return redisTemplate.opsForValue().get(EMAIL_KEY_PREFIX + email) != null;
    }

}
