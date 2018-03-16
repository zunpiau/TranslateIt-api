package zjp.translateit.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zjp.translateit.SpringMvcBaseTest;
import zjp.translateit.service.EmailService;
import zjp.translateit.service.FeedbackService;
import zjp.translateit.web.request.FeedbackRequest;

import static org.mockito.Mockito.mock;

public class FeedbackControllerTest extends SpringMvcBaseTest {

    private FeedbackController controller = new FeedbackController(mock(FeedbackService.class), mock(EmailService.class));

    @Test
    public void feedback() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FeedbackRequest request = new FeedbackRequest("content", "contact", "");
        String ua = "SpringJUnit4ClassRunner";
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
        mockMvc.perform(MockMvcRequestBuilders
                .post("/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(request))
                .header("User-Agent", ua)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("{\"code\":200}"));
    }
}