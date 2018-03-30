package zjp.translateit.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import zjp.translateit.domain.User;
import zjp.translateit.service.UserService;
import zjp.translateit.web.exception.UserExistException;
import zjp.translateit.web.request.PasswordModifyRequest;
import zjp.translateit.web.request.RegisterRequest;
import zjp.translateit.web.response.Response;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response register(@Valid @RequestBody RegisterRequest registerRequest,
            BindingResult result) {
        logger.debug("email [{}] register", registerRequest.getEmail());
        if (result.hasErrors()) {
            return new Response(Response.ResponseCode.INVALID_PARAMETER);
        }
        if (!userService.verifyCodeValid(registerRequest)) {
            return new Response(Response.ResponseCode.VERIFY_CODE_USED);
        }
        userService.registerUser(registerRequest);
        return new Response(Response.ResponseCode.OK);
    }

    @PostMapping(value = "password_modify",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response modifyPassword(@RequestBody @Valid PasswordModifyRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return new Response(Response.ResponseCode.INVALID_PARAMETER);
        }
        logger.debug("user [{}] request modify password", request.getAccount());
        User user = userService.getUser(request.getAccount(), request.getOldPassword());
        if (user == null) {
            return new Response(Response.ResponseCode.INVALID_ACCOUNT);
        }
        if (user.getStatus() == User.Status.DELETE) {
            return new Response(Response.ResponseCode.USER_DELETED);
        }
        return new Response<>(userService.modifyPassword(user.getUid(), request.getNewPassword()));
    }

    @ExceptionHandler(UserExistException.class)
    public Response usernameRegister() {
        return new Response(Response.ResponseCode.USERNAME_REGISTERED);
    }

}
