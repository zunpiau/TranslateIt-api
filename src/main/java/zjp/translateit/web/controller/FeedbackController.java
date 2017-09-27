package zjp.translateit.web.controller;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import zjp.translateit.service.EmailService;
import zjp.translateit.service.FeedbackService;
import zjp.translateit.web.request.FeedbackRequest;
import zjp.translateit.web.response.Response;

@RestController
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final EmailService emailService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService, EmailService emailService) {
        this.feedbackService = feedbackService;
        this.emailService = emailService;
    }

    @RequestMapping(value = "/feedback",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Response feedback(@RequestBody FeedbackRequest request, @RequestHeader("User-Agent") String ua) {
        if (!request.getContent().equals("")) {
            feedbackService.addFeedback(request, ua);
            try {
                emailService.sendFeedbackEmail(request, ua);
            } catch (ClientException e) {
                e.printStackTrace();
            }
            return new Response(Response.ResponseCode.OK);
        } else return new Response(Response.ResponseCode.INVALID_PARAMETER);
    }

}
