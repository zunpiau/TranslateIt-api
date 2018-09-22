package zjp.translateit.web.controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import zjp.translateit.service.FeedbackService;
import zjp.translateit.test.SpringMvcTest;
import zjp.translateit.web.request.FeedbackRequest;

@ContextConfiguration(classes = GlobalControllerTest.Config.class)
public class GlobalControllerTest extends SpringMvcTest {

    @Autowired
    WebApplicationContext context;

    @Test
    public void serverError() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        FeedbackRequest feedbackRequest = new FeedbackRequest("content", "contact", "1.0");
        byte[] bytes = mapper.writeValueAsString(feedbackRequest).getBytes();
        MockMvcBuilders.webAppContextSetup(context)
                .build()
                .perform(MockMvcRequestBuilders.post("/feedback")
                        .content(bytes)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.USER_AGENT, "JUnit"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"code\":500}"));
    }

    @Configuration
    public static class Config {

        @Bean
        @Primary
        public FeedbackService feedbackService() {
            FeedbackService mock = Mockito.mock(FeedbackService.class);
            try {
                Mockito.doThrow(new Exception("Mock exception"))
                        .when(mock)
                        .addFeedback(Mockito.any(), Mockito.anyString());
            } catch (Exception ignore) {
            }
            return mock;
        }
    }
}