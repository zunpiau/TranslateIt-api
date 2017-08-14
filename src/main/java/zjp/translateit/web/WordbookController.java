package zjp.translateit.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zjp.translateit.data.WordbookRepository;
import zjp.translateit.util.EncryptUtil;
import zjp.translateit.web.domain.BackupRequest;
import zjp.translateit.web.domain.RecoverRequest;
import zjp.translateit.web.domain.Token;

@RestController
@RequestMapping("/wordbook")
public class WordbookController {

    private WordbookRepository repository;

    @Autowired
    public void setRepository(WordbookRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/backup",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public String back(@RequestBody BackupRequest backupRequest) {
        Token token = backupRequest.getToken();
        String str = token.getId() + "qwert" + token.getTimestamp();
        String key = EncryptUtil.getMD5(str);
        if (token.getKey().equals(key)) {
            return "{\"mes\":\"" + token.getKey() + "  tr  " + key + "\"}";
        } else
            return "{\"mes\":\"" + token.getKey() + "  err  " + key + "\"}";
    }

    @RequestMapping(value = "/recover",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public String recover(@RequestBody RecoverRequest requestBody) {
        Token token = requestBody.getToken();
        String str = token.getId() + "qwert" + token.getTimestamp();
        String key = EncryptUtil.getMD5(str);
        if (token.getKey().equals(key)) {
            return "{\"mes\":\"" + token.getKey() + "  tr  " + requestBody.getDate() + key + "\"}";
        } else
            return "{\"mes\":\"" + token.getKey() + "  err  " + key + "\"}";
    }

}
