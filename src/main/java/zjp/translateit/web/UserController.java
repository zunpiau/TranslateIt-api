package zjp.translateit.web;

import com.aliyuncs.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import zjp.translateit.domain.User;
import zjp.translateit.service.TokenService;
import zjp.translateit.service.UserService;
import zjp.translateit.web.domain.*;
import zjp.translateit.web.exception.BadRequestException;
import zjp.translateit.web.exception.InnerException;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    @SuppressWarnings("FieldCanBeLocal")
    private final long REQUEST_EXPIRE = 5 * 60 * 1000;    //5min
    private UserService userService;
    private TokenService tokenService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @RequestMapping(value = "/getVerifyCode",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response getVerifyCode(@Valid @RequestBody VerifyCodeRequest request, BindingResult result) {
        logger.debug("email " + request.getEmail() + " request verify");
        if (result.hasErrors())
            throw new BadRequestException("参数不合法");
        if (!userService.checkVerifyCodeSign(request))
            throw new BadRequestException("签名错误");
        if ((System.currentTimeMillis() - request.getTimestamp()) > REQUEST_EXPIRE)
            throw new BadRequestException("请求过期");
        if (userService.forbidGetVerifyCode(request.getEmail()))
            throw new BadRequestException("请检查你的邮箱，或五分钟后再试");
        if (userService.emailRegistered(request.getEmail()))
            throw new BadRequestException("该邮箱已被注册");

        try {
            userService.sendVerifyCode(request);
        } catch (IOException | ClientException e) {
            throw new InnerException("邮件发送失败");
        }
        return Response.getResponseOK();
    }

    @RequestMapping(value = "/login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Token login(@RequestBody LoginRequest request) {
        logger.debug("user " + request.getName() + " login");
        User user = userService.getUserFromLoginRequest(request);
        if (user == null) {
            throw new BadRequestException("用户名和密码不匹配");
        }
        if (user.getStatus() == User.STATUS.DELETE)
            throw new BadRequestException(BadRequestException.MESSAGE_USER_DELETED);
        return tokenService.generateToken(user.getUid());
    }

    @RequestMapping(value = "/token",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Token token(@RequestBody Token token) {
        if (!tokenService.checkToken(token)) {
            throw new BadRequestException("");
        }
        if (tokenService.isTokenUsed(token)) {
            tokenService.setAllTokenUsed(token.getUid());
            throw new BadRequestException("请重新登录");
        }
        return tokenService.generateToken(token);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Token register(@Valid @RequestBody UserForm userForm, BindingResult result) {
        logger.debug("email " + userForm.getEmail() + " register");
        if (!userService.checkVerifyCode(userForm))
            throw new BadRequestException("验证码不存在或与邮箱不匹配");
        if (result.hasErrors())
            throw new BadRequestException("参数不合法");
        if (userService.hasUser(userForm.getName()))
            throw new BadRequestException(BadRequestException.MESSAGE_USER_REGISTERED);

        int uid = userService.registerUser(userForm);
        return tokenService.generateToken(uid);
    }

//    @RequestMapping(value = "/retrieve",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public String retrieve(@RequestBody User user) {
//        return "";
//    }
}
