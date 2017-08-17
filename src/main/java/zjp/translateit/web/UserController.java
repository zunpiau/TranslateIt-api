package zjp.translateit.web;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import zjp.translateit.domain.User;
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

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/getVerifyCode",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response getVerifyCode(@Valid @RequestBody VerifyCodeRequest request, BindingResult result) {
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
            if (!userService.sendVerifyCode(request))
                throw new InnerException("邮件发送失败");
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
        User user = userService.getUserFromLoginRequest(request);
        if (user == null) {
            throw new BadRequestException("用户名和密码不匹配");
        }
        if (user.getStatus() == User.STATUS.DELETE)
            throw new BadRequestException(BadRequestException.MESSAGE_USER_DELETED);
        return userService.generateToken(user.getId());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response register(@Valid @RequestBody UserForm userForm, BindingResult result) {

        if (!userService.checkVerifyCode(userForm))
            throw new BadRequestException("验证码不存在或与邮箱不匹配");
        if (result.hasErrors())
            throw new BadRequestException("参数不合法");
        if (userService.hasUser(userForm.getName()))
            throw new BadRequestException(BadRequestException.MESSAGE_USER_REGISTERED);

        userService.registerUser(userForm);
        return Response.getResponseOK();
    }

//    @RequestMapping(value = "/retrieve",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public String retrieve(@RequestBody User user) {
//        return "";
//    }
}
