package zjp.translateit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import zjp.translateit.data.ManageRepository;
import zjp.translateit.domain.Donation;
import zjp.translateit.dto.SystemCounter;
import zjp.translateit.util.EncryptUtil;

import java.time.Instant;
import java.util.Base64;

@Service
@PropertySource(value = "classpath:application.properties")
public class ManageService {

    private final ManageRepository repository;
    @Value("${manager.salt.token}")
    private String tokenSalt;

    @Autowired
    public ManageService(ManageRepository repository) {
        this.repository = repository;
    }

    public SystemCounter getSystemCounter() {
        return new SystemCounter(
                repository.countUser(),
                repository.countWordbook(),
                repository.countDaily(7),
                repository.countHourly()
        );
    }

    public String login(String name) {
        long currentTime = Instant.now().getEpochSecond();
        String key = EncryptUtil.hash(EncryptUtil.Algorithm.SHA256, name + tokenSalt + currentTime);
        return Base64.getEncoder().encodeToString((name + "." + currentTime + "." + key).getBytes());
    }

    public void addDonation(Donation donation) {
        repository.saveDonation(donation);
    }

}
