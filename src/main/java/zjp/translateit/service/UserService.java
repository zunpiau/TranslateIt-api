package zjp.translateit.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import zjp.translateit.data.UserRepository;
import zjp.translateit.domain.User;
import zjp.translateit.util.EncryptUtil;
import zjp.translateit.web.domain.LoginRequest;
import zjp.translateit.web.domain.Token;
import zjp.translateit.web.domain.UserForm;
import zjp.translateit.web.domain.VerifyCodeRequest;
import zjp.translateit.web.exception.InnerException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Service
@PropertySource(value = "classpath:app.props")
public class UserService {

    @SuppressWarnings("FieldCanBeLocal")
    private final UserRepository repository;
    private final StringRedisTemplate redisTemplate;
    private final String EMAIL_KEY_PREFIX = "email:";
    private final String VERIFY_CODE_KEY_PREFIX = "verify.code:";

    @Value("${salt.password.suffix}")
    private String saltSuffix;
    @Value("${salt.password.prefix}")
    private String saltPrefix;
    @Value("${salt.token}")
    private String tokenSalt;
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

    @Autowired
    public UserService(StringRedisTemplate redisTemplate, UserRepository repository) {
        this.redisTemplate = redisTemplate;
        this.repository = repository;
    }

    public User getUserFromLoginRequest(LoginRequest request) {
        return repository.findUserByNameAndPassword(request.getName(), encryptPassword(request.getPassword()));
    }

    private String encryptPassword(String password) {
        return EncryptUtil.getMD5(saltPrefix + password + saltSuffix);
    }

    public Token generateToken(long id) {
        long currentTime = System.currentTimeMillis();
        String key = EncryptUtil.getMD5("" + id + tokenSalt + currentTime);
        return new Token(id, currentTime, key);
    }

    public void deleteUser(long id) {
        repository.updateUserStatus(id, User.STATUS.DELETE);
    }

    public void registerUser(UserForm userForm) {
        repository.add(new User(userForm.getName(), encryptPassword(userForm.getPassword()), userForm.getEmail()));
        redisTemplate.delete(VERIFY_CODE_KEY_PREFIX + userForm.getEmail());
        redisTemplate.delete(EMAIL_KEY_PREFIX + userForm.getEmail());
    }

    public boolean hasUser(String name) {
        return repository.findUserByName(name) != null;
    }

    public boolean emailRegistered(String email) {
        return repository.findUserByEmail(email) != null;
    }

    public boolean checkVerifyCodeSign(VerifyCodeRequest request) {
        String raw = request.getEmail() + "." + verifySalt + "." + request.getTimestamp();
        String sign = EncryptUtil.getMD5(raw);
        return request.getSign().equals(sign);
    }

    public boolean checkVerifyCode(UserForm userForm) {
        String verifyCode = redisTemplate.opsForValue().get(VERIFY_CODE_KEY_PREFIX + userForm.getEmail());
        return userForm.getVerifyCode().equals(verifyCode);
    }

    public void sendVerifyCode(VerifyCodeRequest request) throws InnerException {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        String verifyCode = generator.generate(9);
        String email = request.getEmail();
        aliEmail(email, verifyCode);
        redisTemplate.opsForValue().set(VERIFY_CODE_KEY_PREFIX + email, verifyCode, 30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(EMAIL_KEY_PREFIX + email, "", 5, TimeUnit.MINUTES);
    }

    private void aliEmail(String mailTo, String verifyCode) throws InnerException {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliKey, aliSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        String template;
        try {
            template = new String(Files.readAllBytes(emailTemplate.getFile().toPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InnerException("邮件发送失败");
        }
        String content = MessageFormat.format(template, verifyCode);
        request.setAccountName(aliAccount);
        request.setFromAlias("TranslateIt");
        request.setAddressType(1);
        request.setReplyToAddress(true);
        request.setToAddress(mailTo);
        request.setSubject("邮箱验证");
        request.setHtmlBody(content);
        HttpResponse response;
        try {
            response = client.doAction(request, true, 2, profile);
        } catch (ClientException e) {
            e.printStackTrace();
            throw new InnerException("邮件发送失败");
        }
        if (!response.isSuccess())
            throw new InnerException("邮件发送失败");
    }

    public boolean forbidGetVerifyCode(String email) {
        return redisTemplate.opsForValue().get(EMAIL_KEY_PREFIX + email) != null;
    }

}
