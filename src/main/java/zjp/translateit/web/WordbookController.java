package zjp.translateit.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zjp.translateit.domain.Wordbook;
import zjp.translateit.service.UserService;
import zjp.translateit.service.WordbookService;
import zjp.translateit.web.domain.BackupRequest;
import zjp.translateit.web.domain.RecoverRequest;
import zjp.translateit.web.domain.Response;
import zjp.translateit.web.domain.Token;
import zjp.translateit.web.exception.BadRequestException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/wordbook")
public class WordbookController {

    @SuppressWarnings("FieldCanBeLocal")
    private final long TOKEN_EXPIRE = 10 * 60 * 1000;

    private WordbookService wordbookService;
    private UserService userService;

    @Autowired
    public WordbookController(WordbookService wordbookService, UserService userService) {
        this.wordbookService = wordbookService;
        this.userService = userService;
    }

    @RequestMapping(value = "/backup",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response backup(@Valid @RequestBody BackupRequest backupRequest, BindingResult result) {
        Token token = backupRequest.getToken();
        long uid = token.getId();
        checkParameter(backupRequest.getToken(), result);
        wordbookService.deleteWordbooks(uid, backupRequest.getWords());
        wordbookService.insertOrUpdate(uid, backupRequest.getWordbooks());
        return Response.getResponseOK();
    }

    private void checkParameter(Token token, BindingResult result) {
        if (result.hasErrors() || !userService.checkToken(token))
            throw new BadRequestException("Token 错误");
        if ((System.currentTimeMillis() - token.getTimestamp()) > TOKEN_EXPIRE)
            throw new BadRequestException("Token 过期");
    }

    @RequestMapping(value = "/recover",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Wordbook> recover(@Valid @RequestBody RecoverRequest requestBody, BindingResult result) {
        checkParameter(requestBody.getToken(), result);
        return wordbookService.getWordbooksMissing(requestBody.getToken().getId(), requestBody.getWords());
    }
}
