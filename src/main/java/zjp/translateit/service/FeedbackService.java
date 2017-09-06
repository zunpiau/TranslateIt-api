package zjp.translateit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import zjp.translateit.web.request.FeedbackRequest;

@Service
public class FeedbackService {

    private final JdbcTemplate template;

    @Autowired
    public FeedbackService(JdbcTemplate template) {
        this.template = template;
    }

    public void addFeedback(FeedbackRequest request, String ua) {
        template.update("insert into feedback (content, contact, ua) values (?, ? , ?) ",
                request.getContent(), request.getContact(), ua);
    }

}
