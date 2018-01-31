package zjp.translateit.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import zjp.translateit.service.WordbookService;
import zjp.translateit.web.request.BackupRequest;
import zjp.translateit.web.response.Response;

import javax.validation.Valid;
import java.util.List;

import static zjp.translateit.Constant.HEADER_UID;

@RestController
@RequestMapping("/wordbook")
public class WordbookController {

    private final WordbookService wordbookService;

    @Autowired
    public WordbookController(WordbookService wordbookService) {
        this.wordbookService = wordbookService;
    }

    @RequestMapping(value = "/backup",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response backup(@RequestHeader(name = HEADER_UID) long uid, @Valid @RequestBody BackupRequest backupRequest,
            BindingResult result) {
        if (result.hasErrors()) {
            return new Response(Response.ResponseCode.INVALID_PARAMETER);
        }
        return new Response<>(wordbookService.backup(uid,
                backupRequest.getWords(),
                backupRequest.getWordbooks()));
    }

    @RequestMapping(value = "/recover",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response recover(@RequestHeader(name = HEADER_UID) long uid, @Valid @RequestBody List<String> words,
            BindingResult result) {
        if (result.hasErrors()) {
            return new Response(Response.ResponseCode.INVALID_PARAMETER);
        }
        return new Response<>(wordbookService.getWordbooksMissing(uid, words));
    }
}
