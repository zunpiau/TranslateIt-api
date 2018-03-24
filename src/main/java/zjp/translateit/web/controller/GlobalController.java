package zjp.translateit.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class GlobalController {

    @RequestMapping("*")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound() {
        return "404";
    }

}
