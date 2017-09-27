package zjp.translateit.web.controller;

import com.aliyuncs.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import zjp.translateit.domain.Token;
import zjp.translateit.domain.User;
import zjp.translateit.service.TokenService;
import zjp.translateit.service.UserService;
import zjp.translateit.web.request.LoginRequest;
import zjp.translateit.web.request.RegisterRequest;
import zjp.translateit.web.request.VerifyCodeRequest;
import zjp.translateit.web.response.Response;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    @SuppressWarnings("FieldCanBeLocal")
    private final long REQUEST_EXPIRE = 60 * 1000;    //5min
    private UserService userService;
    private TokenService tokenService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @RequestMapping(value = "/verify",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response getVerifyCode(@Valid @RequestBody VerifyCodeRequest request, BindingResult result) {
        logger.debug("email " + request.getEmail() + " request verify");
        if (result.hasErrors())
            return new Response(Response.ResponseCode.INVALID_PARAMETER);
        if (!userService.checkVerifyCodeSign(request))
            return new Response(Response.ResponseCode.BAD_SIGN);
        if ((System.currentTimeMillis() - request.getTimestamp()) > REQUEST_EXPIRE)
            return new Response(Response.ResponseCode.REQUIRE_EXPIRED);
        if (userService.forbidGetVerifyCode(request.getEmail()))
            return new Response(Response.ResponseCode.REQUIRE_FRA);
        if (userService.emailRegistered(request.getEmail()))
            return new Response(Response.ResponseCode.EMAIL_REGISTERED);

        try {
            userService.sendVerifyCode(request);
        } catch (IOException | ClientException e) {
            return new Response(Response.ResponseCode.INNER_EXCEPTION);
        }
        return new Response(Response.ResponseCode.OK);
    }

    @RequestMapping(value = "/login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response login(@RequestBody LoginRequest request) {
        logger.debug("user " + request.getName() + " login");
        User user = userService.getUserFromLoginRequest(request);
        if (user == null) {
            return new Response(Response.ResponseCode.INVALID_ACCOUNT);
        }
        if (user.getStatus() == User.STATUS.DELETE)
            return new Response(Response.ResponseCode.USER_DELETED);
        return new Response<>(tokenService.getNewToken(user.getUid()));
    }

    @RequestMapping(value = "/token",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response token(@RequestBody @Valid Token token, BindingResult result) {
        if (result.hasErrors() || !tokenService.checkToken(token)) {
            return new Response(Response.ResponseCode.BAD_TOKEN);
        }
        Token newToken = tokenService.getNewToken(token);
        if (newToken == null)
            return new Response(Response.ResponseCode.RE_LOGIN);
        return new Response<>(newToken);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult result) {
        logger.debug("email " + registerRequest.getEmail() + " register");
        if (!userService.checkVerifyCode(registerRequest))
            return new Response(Response.ResponseCode.BAD_VERIFY_CODE);
        if (result.hasErrors())
            return new Response(Response.ResponseCode.INVALID_PARAMETER);
        if (userService.hasUser(registerRequest.getName()))
            return new Response(Response.ResponseCode.USERNAME_REGISTERED);

        int uid = userService.registerUser(registerRequest);
        return new Response<>(tokenService.getNewToken(uid));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Response usernameRegister() {
        return new Response(Response.ResponseCode.USERNAME_REGISTERED);
    }


//    @RequestMapping(value = "/retrieve",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public String retrieve(@RequestBody User user) {
//        return "";
//    }
}
