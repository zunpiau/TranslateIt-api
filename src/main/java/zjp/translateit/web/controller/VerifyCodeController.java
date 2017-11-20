package zjp.translateit.web.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import zjp.translateit.service.UserService;
import zjp.translateit.web.request.VerifyCodeRequest;
import zjp.translateit.web.response.Response;

import javax.validation.Valid;

@RequestMapping("/verifyCode")
@RestController
public class VerifyCodeController {

    @SuppressWarnings("FieldCanBeLocal")
    // 1 min
    private final long REQUEST_EXPIRE = 60 * 1000;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public VerifyCodeController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response getVerifyCode(@Valid @RequestBody VerifyCodeRequest request,
            BindingResult result) {
        logger.debug("email " + request.getEmail() + " request verify");
        if (result.hasErrors()) {
            return new Response(Response.ResponseCode.INVALID_PARAMETER);
        }
        if (!userService.checkRequestSign(request)) {
            return new Response(Response.ResponseCode.BAD_SIGN);
        }
        if ((System.currentTimeMillis() - request.getTimestamp()) > REQUEST_EXPIRE) {
            return new Response(Response.ResponseCode.REQUIRE_EXPIRED);
        }
        if (userService.forbidGetVerifyCode(request.getEmail())) {
            return new Response(Response.ResponseCode.REQUIRE_FRA);
        }
        if (userService.emailRegistered(request.getEmail())) {
            return new Response(Response.ResponseCode.EMAIL_REGISTERED);
        }

        userService.sendVerifyCode(request);
        return new Response(Response.ResponseCode.OK);
    }
}
