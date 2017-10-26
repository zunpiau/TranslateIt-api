package zjp.translateit.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import zjp.translateit.service.UserService;
import zjp.translateit.web.request.RegisterRequest;
import zjp.translateit.web.response.Response;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST,
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

        return new Response(Response.ResponseCode.OK);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Response usernameRegister() {
        return new Response(Response.ResponseCode.USERNAME_REGISTERED);
    }

}
