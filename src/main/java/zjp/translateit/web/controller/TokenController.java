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

import static zjp.translateit.Constant.ATTRIBUTE_TOKEN;

@RestController
@RequestMapping("/token")
public class TokenController {

    private final UserService userService;
    private final TokenService tokenService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public TokenController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response getToken(@RequestBody @Valid LoginRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return new Response(Response.ResponseCode.INVALID_PARAMETER);
        }
        logger.debug("user " + request.getName() + " login");
        User user = userService.getUserFromLoginRequest(request);
        if (user == null) {
            return new Response(Response.ResponseCode.INVALID_ACCOUNT);
        }
        if (user.getStatus() == User.Status.DELETE) {
            return new Response(Response.ResponseCode.USER_DELETED);
        }
        return new Response<>(tokenService.getNewToken(user.getUid()));
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response refreshToken(@RequestAttribute(name = ATTRIBUTE_TOKEN) Token token) {
        Token newToken = tokenService.refreshToken(token);
        if (newToken == null) {
            return new Response(Response.ResponseCode.RE_LOGIN);
        }
        return new Response<>(newToken);
    }

}
