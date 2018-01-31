package zjp.translateit.web.Interceptor;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import zjp.translateit.SpringMvcTest;

import static zjp.translateit.Constant.*;

@SpringMvcTest
public class TokenInterceptorTest {

    @Autowired
    private WebApplicationContext context;

    @Test
    public void preHandle() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.put("/token/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HEADER_UID, "14234143336132")
                .header(HEADER_TIMESTAMP, "1517372134340")
                .header(HEADER_SIGN, "error sign")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

}