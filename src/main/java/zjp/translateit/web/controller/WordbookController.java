package zjp.translateit.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import zjp.translateit.domain.Wordbook;
import zjp.translateit.dto.ModifyWordbook;
import zjp.translateit.service.WordbookService;
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

    @RequestMapping(value = "/delete",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response delete(@RequestHeader(name = HEADER_UID) long uid, @RequestBody List<String> words) {
        return new Response<>(wordbookService.deleteWordbook(uid, words));
    }

    @RequestMapping(value = "/update",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response update(@RequestHeader(name = HEADER_UID) long uid, @RequestBody List<ModifyWordbook> wordbooks) {
        return new Response<>(wordbookService.updateWordbook(uid, wordbooks));
    }

    @RequestMapping(value = "/add",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response add(@RequestHeader(name = HEADER_UID) long uid, @RequestBody List<Wordbook> wordbooks) {
        return new Response<>(wordbookService.addWordbook(uid, wordbooks));
    }

    @RequestMapping(value = "/words",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getWords(@RequestHeader(name = HEADER_UID) long uid) {
        return new Response<>(wordbookService.getWords(uid));
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
