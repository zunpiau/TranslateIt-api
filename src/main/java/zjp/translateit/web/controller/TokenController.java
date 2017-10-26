package zjp.translateit.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import zjp.translateit.domain.Token;
import zjp.translateit.domain.User;
import zjp.translateit.service.TokenService;
import zjp.translateit.service.UserService;
import zjp.translateit.web.request.LoginRequest;
import zjp.translateit.web.response.Response;

import javax.validation.Valid;

@RestController
@RequestMapping("/token")
public class TokenController {

    private UserService userService;
    private TokenService tokenService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public TokenController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @RequestMapping(value = "/get",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response getToken(@RequestBody LoginRequest request) {
        logger.debug("user " + request.getName() + " login");
        User user = userService.getUserFromLoginRequest(request);
        if (user == null) {
            return new Response(Response.ResponseCode.INVALID_ACCOUNT);
        }
        if (user.getStatus() == User.STATUS.DELETE)
            return new Response(Response.ResponseCode.USER_DELETED);
        return new Response<>(tokenService.getNewToken(user.getUid()));
    }

    @RequestMapping(value = "/refresh",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response refreshToken(@RequestBody @Valid Token token, BindingResult result) {
        if (result.hasErrors() || !tokenService.verifyToken(token)) {
            return new Response(Response.ResponseCode.BAD_TOKEN);
        }
        Token newToken = tokenService.getNewToken(token);
        if (newToken == null)
            return new Response(Response.ResponseCode.RE_LOGIN);
        return new Response<>(newToken);
    }

}
