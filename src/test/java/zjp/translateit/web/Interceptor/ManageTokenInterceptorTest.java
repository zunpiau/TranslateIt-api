package zjp.translateit.web.Interceptor;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import zjp.translateit.test.SpringMvcTest;

@TestPropertySource(value = "classpath:application.properties")
public class ManageTokenInterceptorTest extends SpringMvcTest {

    @Autowired
    private WebApplicationContext context;
    @Value("${manager.name}")
    private String name;
    @Value("${manager.password}")
    private String password;

    @Test
    public void preHandle() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/manage/token")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", name)
                .param("password", password)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));

        mockMvc.perform(MockMvcRequestBuilders.post("/manage/token")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", name)
                .param("password", "wrong")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }
}