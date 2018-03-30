package zjp.translateit.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import zjp.translateit.service.ManageService;
import zjp.translateit.web.response.Response;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static zjp.translateit.Constant.AUTH_TOKEN;

@Controller
@RequestMapping("/manage")
public class ManageController {

    private final ManageService service;

    @Autowired
    public ManageController(ManageService service) {
        this.service = service;
    }

    @ResponseBody
    @GetMapping(value = "/count")
    public Response getSystemCount() {
        return new Response<>(service.getSystemCounter());
    }

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public RedirectView login(@RequestParam String name, @RequestParam String password, HttpServletResponse response) {
        String tokenString = service.login(name, password);
        if (tokenString == null) {
            throw new LoginException();
        } else {
            Cookie cookie = new Cookie(AUTH_TOKEN, tokenString);
            cookie.setPath("/view");
            cookie.setHttpOnly(false);
            response.addCookie(cookie);
            return new RedirectView("/view/manage.html");
        }
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(LoginException.class)
    public void forbidden() {
    }

    private static class LoginException extends RuntimeException {

    }

}
