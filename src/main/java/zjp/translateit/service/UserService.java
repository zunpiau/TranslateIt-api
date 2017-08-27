package zjp.translateit.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zjp.translateit.data.UserRepository;
import zjp.translateit.domain.User;
import zjp.translateit.util.EncryptUtil;
import zjp.translateit.web.domain.LoginRequest;
import zjp.translateit.web.domain.UserForm;
import zjp.translateit.web.domain.VerifyCodeRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Service
@PropertySource(value = "classpath:application.properties")
@PropertySource(value = "classpath:application-${spring.profiles.active}.properties")
public class UserService {


    private final UserRepository repository;
    private final StringRedisTemplate redisTemplate;
    @SuppressWarnings("FieldCanBeLocal")
    private final String EMAIL_KEY_PREFIX = "email:";
    private final String VERIFY_CODE_KEY_PREFIX = "verify.code:";

    @Value("${salt.verify}")
    private String verifySalt;
    @Value("${ali.accessKey}")
    private String aliKey;
    @Value("${ali.accessSecret}")
    private String aliSecret;
    @Value("${ali.emailAccount}")
    private String aliAccount;

    @Value("classpath:email-template.html")
    private Resource emailTemplate;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserService(StringRedisTemplate redisTemplate, UserRepository repository) {
        this.redisTemplate = redisTemplate;
        this.repository = repository;
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
    public int registerUser(UserForm userForm) {
        String passwordSalted = BCrypt.hashpw(userForm.getPassword(), BCrypt.gensalt(10));
        int uid = repository.generateUid();
        repository.add(new User(uid, userForm.getName(), passwordSalted, userForm.getEmail(), User.STATUS.NORMAL));
        redisTemplate.delete(VERIFY_CODE_KEY_PREFIX + userForm.getEmail());
        redisTemplate.delete(EMAIL_KEY_PREFIX + userForm.getEmail());
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

    public boolean checkVerifyCode(UserForm userForm) {
        String verifyCode = redisTemplate.opsForValue().get(VERIFY_CODE_KEY_PREFIX + userForm.getEmail());
        return userForm.getVerifyCode().equals(verifyCode);
    }

    @Transactional
    public void sendVerifyCode(VerifyCodeRequest request) throws IOException, ClientException {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        String verifyCode = generator.generate(9);
        String email = request.getEmail();
        logger.debug("Send email to " + email + " , verifyCode: " + verifyCode);
        if (!aliEmail(email, verifyCode))
            throw new ClientException("");
        redisTemplate.opsForValue().set(VERIFY_CODE_KEY_PREFIX + email, verifyCode, 30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(EMAIL_KEY_PREFIX + email, "", 5, TimeUnit.MINUTES);
    }

    private boolean aliEmail(String mailTo, String verifyCode) throws IOException, ClientException {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliKey, aliSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        String template;
        template = new String(Files.readAllBytes(emailTemplate.getFile().toPath()), StandardCharsets.UTF_8);
        String content = MessageFormat.format(template, verifyCode);
        request.setAccountName(aliAccount);
        request.setFromAlias("TranslateIt");
        request.setAddressType(1);
        request.setReplyToAddress(true);
        request.setToAddress(mailTo);
        request.setSubject("邮箱验证");
        request.setHtmlBody(content);
        HttpResponse response;
        response = client.doAction(request, true, 2, profile);
        return response.isSuccess();
    }

    public boolean forbidGetVerifyCode(String email) {
        return redisTemplate.opsForValue().get(EMAIL_KEY_PREFIX + email) != null;
    }

}
