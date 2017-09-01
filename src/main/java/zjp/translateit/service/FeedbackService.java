package zjp.translateit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    private final JdbcTemplate template;

    @Autowired
    public FeedbackService(JdbcTemplate template) {
        this.template = template;
    }

    public void addFeedback(String content, String contact, String ua) {
        template.update("insert into feedback (content, contact, ua) values (?, ? , ?) ",
                content, contact, ua);
    }

}
