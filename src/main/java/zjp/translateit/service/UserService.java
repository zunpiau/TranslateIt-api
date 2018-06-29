package zjp.translateit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zjp.translateit.data.TokenRepository;
import zjp.translateit.data.UserRepository;
import zjp.translateit.domain.User;
import zjp.translateit.util.RandomStringGenerator;
import zjp.translateit.util.UidGenerator;
import zjp.translateit.web.exception.UserExistException;
import zjp.translateit.web.request.RegisterRequest;

import javax.annotation.Nullable;

@Service
@PropertySource(value = "classpath:application.properties")
public class UserService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService;
    private final UidGenerator uidGenerator;
    private final RandomStringGenerator generator;

    private final String EMAIL_KEY_PREFIX = "email:";
    private final String VERIFY_CODE_KEY_PREFIX = "verify.code:";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserService(StringRedisTemplate redisTemplate,
            UserRepository repository,
            TokenRepository tokenRepository,
            EmailService emailService,
            UidGenerator uidGenerator,
            RandomStringGenerator generator) {
        this.redisTemplate = redisTemplate;
        this.repository = repository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.uidGenerator = uidGenerator;
        this.generator = generator;
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
        if (repository.hasUser(registerRequest.getName())) {
            throw new UserExistException();
        }
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
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            ((StringRedisConnection) connection).del(VERIFY_CODE_KEY_PREFIX + registerRequest.getEmail(),
                    EMAIL_KEY_PREFIX + registerRequest.getEmail());
            return null;
        });

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
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            StringRedisConnection stringRedisConn = (StringRedisConnection) connection;
            stringRedisConn.set(VERIFY_CODE_KEY_PREFIX + email, verifyCode, Expiration.seconds(1800), RedisStringCommands.SetOption.UPSERT);
            stringRedisConn.set(EMAIL_KEY_PREFIX + email, "", Expiration.seconds(60), RedisStringCommands.SetOption.UPSERT);
            return null;
        });
    }

    @SuppressWarnings("ConstantConditions")
    public boolean forbidGetVerifyCode(String email) {
        return redisTemplate.hasKey(EMAIL_KEY_PREFIX + email);
    }

    @Transactional
    public int modifyPassword(long uid, String newPassword) {
        tokenRepository.removeAll(uid);
        return repository.modifyPassword(uid, BCrypt.hashpw(newPassword, BCrypt.gensalt(10)));
    }

    @Transactional
    public int modifyUserName(long uid, String newUseName) {
        if (repository.hasUser(newUseName)) {
            throw new UserExistException();
        }
        try {
            return repository.modifyUserName(uid, newUseName);
        } catch (DuplicateKeyException e) {
            logger.debug(e.getMessage());
            throw new UserExistException();
        }
    }

}
