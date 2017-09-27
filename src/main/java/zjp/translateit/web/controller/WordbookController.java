package zjp.translateit.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zjp.translateit.domain.Token;
import zjp.translateit.service.TokenService;
import zjp.translateit.service.WordbookService;
import zjp.translateit.web.request.BackupRequest;
import zjp.translateit.web.request.RecoverRequest;
import zjp.translateit.web.response.Response;

import javax.validation.Valid;

@RestController
@RequestMapping("/wordbook")
public class WordbookController {

    @SuppressWarnings("FieldCanBeLocal")
    private final long TOKEN_EXPIRE = 5 * 60 * 1000;

    private WordbookService wordbookService;
    private TokenService tokenService;

    @Autowired
    public WordbookController(WordbookService wordbookService, TokenService tokenService) {
        this.wordbookService = wordbookService;
        this.tokenService = tokenService;
    }

    @RequestMapping(value = "/backup",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response backup(@Valid @RequestBody BackupRequest backupRequest, BindingResult result) {
        Token token = backupRequest.getToken();
        long uid = token.getUid();
        Response response = checkParameter(backupRequest.getToken(), result);
        if (response != null)
            return response;
        wordbookService.backupWordbook(uid, backupRequest.getWords(), backupRequest.getWordbooks());
        return new Response(Response.ResponseCode.OK);
    }

    private Response checkParameter(Token token, BindingResult result) {
        if (result.hasErrors() || !tokenService.checkToken(token))
            return new Response(Response.ResponseCode.BAD_TOKEN);
        if ((System.currentTimeMillis() - token.getTimestamp()) > TOKEN_EXPIRE)
            return new Response(Response.ResponseCode.TOKEN_EXPIRED);
        return null;
    }

    @RequestMapping(value = "/recover",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response recover(@Valid @RequestBody RecoverRequest requestBody, BindingResult result) {
        Response response = checkParameter(requestBody.getToken(), result);
        if (response != null)
            return response;
        return new Response<>(wordbookService.getWordbooksMissing(requestBody.getToken().getUid(), requestBody.getWords()));
    }
}
