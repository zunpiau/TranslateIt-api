package zjp.translateit.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import zjp.translateit.domain.Token;
import zjp.translateit.service.InviteCodeService;
import zjp.translateit.service.TokenService;
import zjp.translateit.service.UserService;
import zjp.translateit.web.exception.InviteCodeUsedException;
import zjp.translateit.web.exception.UserExistException;
import zjp.translateit.web.request.RegisterRequest;
import zjp.translateit.web.response.Response;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final InviteCodeService inviteCodeService;
    private final TokenService tokenService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserController(UserService userService,
            InviteCodeService inviteCodeService,
            TokenService tokenService) {
        this.userService = userService;
        this.inviteCodeService = inviteCodeService;
        this.tokenService = tokenService;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/inviteCode",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getInviteCode(@Valid @RequestBody Token token, BindingResult result) {
        if (result.hasErrors()) {
            return new Response(Response.ResponseCode.INVALID_PARAMETER);
        }
        if (!tokenService.verifyToken(token)) {
            return new Response(Response.ResponseCode.BAD_TOKEN);
        }
        return new Response<>(inviteCodeService.getInviteCode(token.getUid()));
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response register(@Valid @RequestBody RegisterRequest registerRequest,
            BindingResult result) {
        logger.debug("email " + registerRequest.getEmail() + " register");
        if (result.hasErrors()) {
            return new Response(Response.ResponseCode.INVALID_PARAMETER);
        }
        if (!userService.verifyCodeValid(registerRequest)) {
            return new Response(Response.ResponseCode.VERIFY_CODE_USED);
        }
        if (userService.hasUser(registerRequest.getName())) {
            return new Response(Response.ResponseCode.USERNAME_REGISTERED);
        }
        if (inviteCodeService.isInviteCodeUsed(registerRequest.getInviteCode())) {
            return new Response(Response.ResponseCode.INVITE_CODE_USED);
        }
        userService.registerUser(registerRequest);
        return new Response(Response.ResponseCode.OK);
    }

    @ExceptionHandler(UserExistException.class)
    public Response usernameRegister() {
        return new Response(Response.ResponseCode.USERNAME_REGISTERED);
    }

    @ExceptionHandler(InviteCodeUsedException.class)
    public Response inviteCodeUsed() {
        return new Response(Response.ResponseCode.INVITE_CODE_USED);
    }

}
