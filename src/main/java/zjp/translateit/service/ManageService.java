package zjp.translateit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import zjp.translateit.data.ManageRepository;
import zjp.translateit.dto.SystemCounter;
import zjp.translateit.util.EncryptUtil;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Base64;

@Service
@PropertySource(value = "classpath:application.properties")
public class ManageService {

    private final ManageRepository repository;
    @Value("${salt.token.manager}")
    private String tokenSalt;
    @Value("${salt.account.manager}")
    private String accountSalt;
    @Value("${manager.name}")
    private String rootName;
    @Value("${manager.password.hashed}")
    private String passwordHashed;

    @Autowired
    public ManageService(ManageRepository repository) {
        this.repository = repository;
    }

    public SystemCounter getSystemCounter() {
        return new SystemCounter(
                repository.countUser(),
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
            return generateToken(name);
        } else {
            return null;
        }
    }

    private String generateToken(String name) {
        long currentTime = Instant.now().getEpochSecond();
        String key = EncryptUtil.hash(EncryptUtil.Algorithm.SHA256, name + tokenSalt + currentTime);
        return Base64.getEncoder().encodeToString((name + "." + currentTime + "." + key).getBytes());
    }
}
