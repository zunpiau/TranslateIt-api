package zjp.translateit.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import zjp.translateit.test.SpringJdbcTest;
import zjp.translateit.web.request.FeedbackRequest;

import static org.junit.Assert.assertEquals;

public class FeedbackServiceTest extends SpringJdbcTest {

    @Autowired
    FeedbackService service;
    @Autowired
    JdbcTemplate template;

    @Test
    public void testAddFeedback() {
        assertEquals(0, JdbcTestUtils.countRowsInTable(template, "feedback"));
        service.addFeedback(new FeedbackRequest("content", "contact", "0.0.1"), "JUnitRunner");
        assertEquals(1, JdbcTestUtils.countRowsInTable(template, "feedback"));
    }
}