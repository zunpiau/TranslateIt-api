package zjp.translateit.web.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import zjp.translateit.service.UserService;
import zjp.translateit.web.response.Response;

import static zjp.translateit.Constant.HEADER_EMAIL;

@RequestMapping("/verifyCode")
@RestController
public class VerifyCodeController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public VerifyCodeController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response getVerifyCode(@RequestHeader(name = HEADER_EMAIL) String email) {
        logger.debug("email " + email + " request verify");
        if (userService.forbidGetVerifyCode(email)) {
            return new Response(Response.ResponseCode.REQUIRE_FRA);
        }
        if (userService.emailRegistered(email)) {
            return new Response(Response.ResponseCode.EMAIL_REGISTERED);
        }
        userService.sendVerifyCode(email);
        return new Response(Response.ResponseCode.OK);
    }
}
